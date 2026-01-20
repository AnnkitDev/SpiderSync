package com.Project.SpiderSync.service;

import com.Project.SpiderSync.entities.Page;
import com.Project.SpiderSync.repositories.PageRepository;
import com.Project.SpiderSync.crawler.ContentExtractor;
import com.Project.SpiderSync.crawler.RobotsTxtService;
import com.Project.SpiderSync.nlp.KeywordExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PageServiceImpl implements PageService {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private IndexingService indexingService;

    @Autowired
    private RobotsTxtService robotsTxtService;

    @Autowired
    private ContentExtractor contentExtractor;

    @Autowired
    private KeywordExtractor keywordExtractor;

    @Value("${crawler.delay.ms:1000}")
    private long crawlDelay;

    @Value("${crawler.max-depth:3}")
    private int maxDepth;

    @Value("${crawler.user-agent}")
    private String userAgent;

    @Value("${crawler.timeout.ms:10000}")
    private int timeout;

    @Value("${crawler.respect-robots-txt:true}")
    private boolean respectRobotsTxt;

    // Memory to prevent the spider from going in circles
    private final Set<String> visitedUrls = new HashSet<>();

    @Override
    @Transactional
    public void savePage(Page page) {
        // Check if DB already has it by URL
        if (!pageRepository.existsByUrl(page.getUrl())) {
            Page savedPage = pageRepository.save(page);

            // Index in Elasticsearch
            try {
                indexingService.indexPage(savedPage);
                log.info("Saved and indexed page: {}", page.getTitle());
            } catch (Exception e) {
                log.error("Error indexing page: {}", e.getMessage());
            }
        } else {
            log.debug("Skipping: URL already exists in database: {}", page.getUrl());
        }
    }

    @Override
    public List<Page> searchPages(String query) {
        // This now uses MySQL full-text search as fallback
        // Primary search should use SearchService with Elasticsearch
        return pageRepository.search(query);
    }

    @Override
    @Async("crawlerExecutor")
    public void startCrawl(String url) {
        startCrawl(url, 0);
    }

    /**
     * Enhanced crawl method with depth tracking
     */
    private void startCrawl(String url, int depth) {
        // Check depth limit
        if (depth > maxDepth) {
            log.debug("Max depth reached for: {}", url);
            return;
        }

        // Memory Check: Stop if we've been here in this session
        if (visitedUrls.contains(url)) {
            return;
        }

        // Normalize URL
        url = normalizeUrl(url);
        if (url == null) {
            return;
        }

        try {
            visitedUrls.add(url);

            // Check robots.txt
            if (respectRobotsTxt && !isAllowedByRobotsTxt(url)) {
                log.info("Blocked by robots.txt: {}", url);
                return;
            }

            // Politeness delay
            if (crawlDelay > 0) {
                Thread.sleep(crawlDelay);
            }

            // Connect and Download
            long startTime = System.currentTimeMillis();
            Document doc = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .timeout(timeout)
                    .get();
            long responseTime = System.currentTimeMillis() - startTime;

            // Extract content
            String title = doc.title();
            if (title == null || title.isEmpty()) {
                title = "Untitled";
            }

            String description = contentExtractor.extractDescription(doc);
            String content = contentExtractor.extractCleanContent(doc);
            String metaKeywords = contentExtractor.extractKeywords(doc);
            String language = contentExtractor.extractLanguage(doc);

            // Extract keywords using NLP
            String extractedKeywords = keywordExtractor.extractKeywords(title, content, 10);
            String keywords = metaKeywords.isEmpty() ? extractedKeywords : metaKeywords + ", " + extractedKeywords;

            // Calculate content hash for deduplication
            String contentHash = DigestUtils.sha256Hex(content);

            // Check for duplicate content
            if (isDuplicateContent(contentHash)) {
                log.debug("Duplicate content detected: {}", url);
                return;
            }

            // Create and save Page
            Page page = new Page();
            page.setUrl(url);
            page.setTitle(title.length() > 500 ? title.substring(0, 497) + "..." : title);
            page.setDescription(description);
            page.setContent(content);
            page.setKeywords(keywords.length() > 500 ? keywords.substring(0, 497) + "..." : keywords);
            page.setLanguage(language);
            page.setContentHash(contentHash);
            page.setStatusCode(200);
            page.setResponseTime((int) responseTime);
            page.setCrawlDepth(depth);
            page.setPageRank(calculateSimplePageRank(doc));
            page.setLastCrawledAt(LocalDateTime.now());

            savePage(page);

            log.info("Indexed [Depth: {}]: {}", depth, title);

            // Discovery: Find all links on the current page
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String nextUrl = link.attr("abs:href");

                // Only follow links that look like web addresses
                if (isValidUrl(nextUrl)) {
                    startCrawl(nextUrl, depth + 1);
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Crawl interrupted for {}: {}", url, e.getMessage());
        } catch (Exception e) {
            log.warn("Could not crawl {}: {}", url, e.getMessage());
        }
    }

    /**
     * Normalize URL (remove fragments, trailing slashes, etc.)
     */
    private String normalizeUrl(String url) {
        try {
            URI uri = new URI(url);
            // Remove fragment
            String normalized = uri.getScheme() + "://" + uri.getHost();
            if (uri.getPort() != -1 && uri.getPort() != 80 && uri.getPort() != 443) {
                normalized += ":" + uri.getPort();
            }
            normalized += uri.getPath();
            if (uri.getQuery() != null) {
                normalized += "?" + uri.getQuery();
            }
            return normalized;
        } catch (Exception e) {
            log.debug("Invalid URL: {}", url);
            return null;
        }
    }

    /**
     * Check if URL is allowed by robots.txt
     */
    private boolean isAllowedByRobotsTxt(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getScheme() + "://" + uri.getHost();
            return robotsTxtService.isAllowed(url, domain);
        } catch (Exception e) {
            return true; // Allow if we can't check
        }
    }

    /**
     * Validate URL
     */
    private boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        // Must start with http or https
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false;
        }

        // Skip common non-HTML resources
        String lowerUrl = url.toLowerCase();
        return !lowerUrl.endsWith(".pdf") &&
                !lowerUrl.endsWith(".jpg") &&
                !lowerUrl.endsWith(".jpeg") &&
                !lowerUrl.endsWith(".png") &&
                !lowerUrl.endsWith(".gif") &&
                !lowerUrl.endsWith(".css") &&
                !lowerUrl.endsWith(".js") &&
                !lowerUrl.endsWith(".xml") &&
                !lowerUrl.endsWith(".zip") &&
                !lowerUrl.endsWith(".exe");
    }

    /**
     * Check for duplicate content
     */
    private boolean isDuplicateContent(String contentHash) {
        // This is a simple check - in production, you might want to query the database
        return false; // For now, allow all
    }

    /**
     * Calculate simple PageRank based on internal links
     */
    private double calculateSimplePageRank(Document doc) {
        Elements links = doc.select("a[href]");
        int internalLinks = 0;
        int externalLinks = 0;

        String currentDomain = doc.location();
        for (Element link : links) {
            String href = link.attr("abs:href");
            if (href.startsWith(currentDomain)) {
                internalLinks++;
            } else if (href.startsWith("http")) {
                externalLinks++;
            }
        }

        // Simple scoring: more internal structure = higher rank
        return Math.min(1.0, (internalLinks * 0.1) + (externalLinks * 0.05));
    }
}
