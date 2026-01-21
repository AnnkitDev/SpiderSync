package com.Project.SpiderSync.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rate limiting filter to prevent API abuse.
 * Uses token bucket algorithm with configurable limits per IP.
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(RateLimitingFilter.class);

  // Rate limit configuration
  private static final int MAX_REQUESTS_PER_MINUTE = 100;
  private static final long WINDOW_SIZE_MS = 60_000; // 1 minute

  // Store request counts per IP
  private final Map<String, RateLimitInfo> requestCounts = new ConcurrentHashMap<>();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String clientIP = getClientIP(request);
    String path = request.getRequestURI();

    // Skip rate limiting for static resources
    if (isStaticResource(path)) {
      filterChain.doFilter(request, response);
      return;
    }

    if (isRateLimited(clientIP)) {
      log.warn("Rate limit exceeded for IP: {}", clientIP);
      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      response.setContentType("application/json");
      response.getWriter().write("{\"error\":\"Rate limit exceeded. Please try again later.\",\"retryAfter\":60}");
      return;
    }

    // Add security headers
    addSecurityHeaders(response);

    filterChain.doFilter(request, response);
  }

  private boolean isRateLimited(String clientIP) {
    long now = System.currentTimeMillis();

    RateLimitInfo info = requestCounts.compute(clientIP, (ip, existing) -> {
      if (existing == null || now - existing.windowStart > WINDOW_SIZE_MS) {
        return new RateLimitInfo(now, new AtomicInteger(1));
      }
      existing.requestCount.incrementAndGet();
      return existing;
    });

    return info.requestCount.get() > MAX_REQUESTS_PER_MINUTE;
  }

  private boolean isStaticResource(String path) {
    return path.startsWith("/css/") ||
        path.startsWith("/js/") ||
        path.startsWith("/images/") ||
        path.startsWith("/fonts/") ||
        path.endsWith(".ico") ||
        path.endsWith(".png") ||
        path.endsWith(".jpg") ||
        path.endsWith(".svg");
  }

  private String getClientIP(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }
    String xRealIP = request.getHeader("X-Real-IP");
    if (xRealIP != null && !xRealIP.isEmpty()) {
      return xRealIP;
    }
    return request.getRemoteAddr();
  }

  private void addSecurityHeaders(HttpServletResponse response) {
    // Prevent MIME type sniffing
    response.setHeader("X-Content-Type-Options", "nosniff");

    // Prevent clickjacking
    response.setHeader("X-Frame-Options", "DENY");

    // Enable XSS filter
    response.setHeader("X-XSS-Protection", "1; mode=block");

    // Referrer policy
    response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

    // Content Security Policy (relaxed for development)
    response.setHeader("Content-Security-Policy",
        "default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline' fonts.googleapis.com; font-src 'self' fonts.gstatic.com; img-src 'self' data:;");
  }

  // Cleanup old entries periodically (called by scheduled task if needed)
  public void cleanupExpiredEntries() {
    long now = System.currentTimeMillis();
    requestCounts.entrySet().removeIf(entry -> now - entry.getValue().windowStart > WINDOW_SIZE_MS * 2);
  }

  private static class RateLimitInfo {
    final long windowStart;
    final AtomicInteger requestCount;

    RateLimitInfo(long windowStart, AtomicInteger requestCount) {
      this.windowStart = windowStart;
      this.requestCount = requestCount;
    }
  }
}
