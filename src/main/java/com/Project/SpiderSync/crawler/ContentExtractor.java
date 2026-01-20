package com.Project.SpiderSync.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContentExtractor {

  private static final Logger log = LoggerFactory.getLogger(ContentExtractor.class);

  /**
   * Extract metadata from HTML document
   */
  public Map<String, String> extractMetadata(Document doc) {
    Map<String, String> metadata = new HashMap<>();

    // Extract Open Graph metadata
    Elements ogTags = doc.select("meta[property^=og:]");
    for (Element tag : ogTags) {
      String property = tag.attr("property");
      String content = tag.attr("content");
      metadata.put(property, content);
    }

    // Extract standard meta tags
    Elements metaTags = doc.select("meta[name]");
    for (Element tag : metaTags) {
      String name = tag.attr("name");
      String content = tag.attr("content");
      metadata.put(name, content);
    }

    return metadata;
  }

  /**
   * Extract description from metadata or generate from content
   */
  public String extractDescription(Document doc) {
    // Try Open Graph description
    Element ogDesc = doc.selectFirst("meta[property=og:description]");
    if (ogDesc != null && !ogDesc.attr("content").isEmpty()) {
      return ogDesc.attr("content");
    }

    // Try meta description
    Element metaDesc = doc.selectFirst("meta[name=description]");
    if (metaDesc != null && !metaDesc.attr("content").isEmpty()) {
      return metaDesc.attr("content");
    }

    // Generate from first paragraph
    Element firstPara = doc.selectFirst("p");
    if (firstPara != null) {
      String text = firstPara.text();
      return text.length() > 200 ? text.substring(0, 197) + "..." : text;
    }

    return "";
  }

  /**
   * Extract keywords from metadata
   */
  public String extractKeywords(Document doc) {
    Element keywordsTag = doc.selectFirst("meta[name=keywords]");
    if (keywordsTag != null) {
      return keywordsTag.attr("content");
    }
    return "";
  }

  /**
   * Clean and extract main content
   */
  public String extractCleanContent(Document doc) {
    // Remove script and style elements
    doc.select("script, style, nav, header, footer, aside").remove();

    // Get body text
    Element body = doc.body();
    if (body != null) {
      return body.text();
    }

    return "";
  }

  /**
   * Extract language from HTML
   */
  public String extractLanguage(Document doc) {
    // Try html lang attribute
    Element html = doc.selectFirst("html");
    if (html != null && html.hasAttr("lang")) {
      String lang = html.attr("lang");
      if (!lang.isEmpty()) {
        // Return first part (e.g., "en" from "en-US")
        return lang.split("-")[0].toLowerCase();
      }
    }

    // Try meta tag
    Element metaLang = doc.selectFirst("meta[http-equiv=content-language]");
    if (metaLang != null) {
      String lang = metaLang.attr("content");
      if (!lang.isEmpty()) {
        return lang.split("-")[0].toLowerCase();
      }
    }

    return "en"; // Default to English
  }
}
