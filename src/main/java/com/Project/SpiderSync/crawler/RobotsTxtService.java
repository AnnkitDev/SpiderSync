package com.Project.SpiderSync.crawler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
public class RobotsTxtService {

  private static final String USER_AGENT = "SpiderSync";

  @Cacheable(value = "robots-txt", key = "#domain")
  public RobotsTxtRules getRobotsTxt(String domain) {
    try {
      String robotsUrl = domain + "/robots.txt";
      URL url = new URL(robotsUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(5000);
      connection.setReadTimeout(5000);

      if (connection.getResponseCode() == 200) {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));

        return parseRobotsTxt(reader);
      }
    } catch (Exception e) {
      log.debug("Could not fetch robots.txt for {}: {}", domain, e.getMessage());
    }

    // If robots.txt doesn't exist or can't be fetched, allow all
    return new RobotsTxtRules(new HashSet<>(), null);
  }

  private RobotsTxtRules parseRobotsTxt(BufferedReader reader) throws Exception {
    Set<String> disallowedPaths = new HashSet<>();
    Integer crawlDelay = null;
    boolean relevantSection = false;

    String line;
    while ((line = reader.readLine()) != null) {
      line = line.trim();

      if (line.isEmpty() || line.startsWith("#")) {
        continue;
      }

      if (line.toLowerCase().startsWith("user-agent:")) {
        String agent = line.substring(11).trim();
        relevantSection = agent.equals("*") || agent.equalsIgnoreCase(USER_AGENT);
      } else if (relevantSection) {
        if (line.toLowerCase().startsWith("disallow:")) {
          String path = line.substring(9).trim();
          if (!path.isEmpty()) {
            disallowedPaths.add(path);
          }
        } else if (line.toLowerCase().startsWith("crawl-delay:")) {
          try {
            crawlDelay = Integer.parseInt(line.substring(12).trim());
          } catch (NumberFormatException e) {
            log.warn("Invalid crawl-delay value: {}", line);
          }
        }
      }
    }

    return new RobotsTxtRules(disallowedPaths, crawlDelay);
  }

  public boolean isAllowed(String url, String domain) {
    RobotsTxtRules rules = getRobotsTxt(domain);

    try {
      URL urlObj = new URL(url);
      String path = urlObj.getPath();

      for (String disallowedPath : rules.getDisallowedPaths()) {
        if (path.startsWith(disallowedPath)) {
          return false;
        }
      }
    } catch (Exception e) {
      log.error("Error checking robots.txt for {}: {}", url, e.getMessage());
    }

    return true;
  }

  public static class RobotsTxtRules {
    private final Set<String> disallowedPaths;
    private final Integer crawlDelay;

    public RobotsTxtRules(Set<String> disallowedPaths, Integer crawlDelay) {
      this.disallowedPaths = disallowedPaths;
      this.crawlDelay = crawlDelay;
    }

    public Set<String> getDisallowedPaths() {
      return disallowedPaths;
    }

    public Integer getCrawlDelay() {
      return crawlDelay;
    }
  }
}
