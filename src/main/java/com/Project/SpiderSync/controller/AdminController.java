package com.Project.SpiderSync.controller;

import com.Project.SpiderSync.entities.Page;
import com.Project.SpiderSync.repositories.PageRepository;
import com.Project.SpiderSync.service.IndexingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private static final Logger log = LoggerFactory.getLogger(AdminController.class);

  private final PageRepository pageRepository;
  private final IndexingService indexingService;

  public AdminController(PageRepository pageRepository, IndexingService indexingService) {
    this.pageRepository = pageRepository;
    this.indexingService = indexingService;
  }

  @PostMapping("/seed-data")
  public ResponseEntity<Map<String, Object>> seedData() {
    Map<String, Object> response = new HashMap<>();

    try {
      // Check if data already exists
      long count = pageRepository.count();
      if (count > 0) {
        response.put("status", "skipped");
        response.put("message", "Database already contains " + count + " pages. Skipping data seeding.");
        response.put("existingPages", count);
        return ResponseEntity.ok(response);
      }

      log.info("Starting manual data seeding...");

      List<Page> pages = new ArrayList<>();

      // Technology Pages
      pages.add(createPage(
          "https://example.com/java-programming",
          "Java Programming Guide",
          "Comprehensive guide to Java programming language covering basics to advanced topics",
          "Java is a high-level, class-based, object-oriented programming language. It is designed to have as few implementation dependencies as possible. Java applications are typically compiled to bytecode that can run on any Java virtual machine (JVM) regardless of the underlying computer architecture. The syntax of Java is similar to C and C++, but has fewer low-level facilities than either of them. Java is one of the most popular programming languages in use, particularly for client-server web applications.",
          "java, programming, jvm, object-oriented, bytecode",
          0.85,
          0));

      pages.add(createPage(
          "https://example.com/python-tutorial",
          "Python Tutorial for Beginners",
          "Learn Python programming from scratch with practical examples",
          "Python is an interpreted, high-level, general-purpose programming language. Created by Guido van Rossum and first released in 1991, Python's design philosophy emphasizes code readability with its notable use of significant whitespace. Its language constructs and object-oriented approach aim to help programmers write clear, logical code for small and large-scale projects. Python is dynamically typed and garbage-collected. It supports multiple programming paradigms, including structured, object-oriented, and functional programming.",
          "python, programming, tutorial, scripting, data science",
          0.90,
          0));

      pages.add(createPage(
          "https://example.com/web-development",
          "Modern Web Development",
          "Complete guide to modern web development technologies and frameworks",
          "Web development refers to the building, creating, and maintaining of websites. It includes aspects such as web design, web publishing, web programming, and database management. Modern web development involves using HTML5, CSS3, and JavaScript frameworks like React, Angular, and Vue.js. Backend development often uses Node.js, Python Django, Ruby on Rails, or Java Spring Boot. Understanding RESTful APIs, databases, and cloud deployment is essential for full-stack developers.",
          "web development, html, css, javascript, react, angular",
          0.88,
          0));

      // Science Pages
      pages.add(createPage(
          "https://example.com/quantum-physics",
          "Introduction to Quantum Physics",
          "Understanding the fundamentals of quantum mechanics and particle physics",
          "Quantum physics is a fundamental theory in physics that provides a description of the physical properties of nature at the scale of atoms and subatomic particles. It is the foundation of all quantum physics including quantum chemistry, quantum field theory, quantum technology, and quantum information science. Classical physics, the description of physics that existed before the theory of relativity and quantum mechanics, describes many aspects of nature at an ordinary scale, while quantum mechanics explains the aspects of nature at small scales.",
          "quantum physics, science, particles, mechanics, atoms",
          0.75,
          0));

      pages.add(createPage(
          "https://example.com/climate-change",
          "Climate Change and Global Warming",
          "Scientific overview of climate change causes and effects",
          "Climate change refers to long-term shifts in temperatures and weather patterns. These shifts may be natural, but since the 1800s, human activities have been the main driver of climate change, primarily due to the burning of fossil fuels like coal, oil, and gas. Burning fossil fuels generates greenhouse gas emissions that act like a blanket wrapped around the Earth, trapping the sun's heat and raising temperatures. The consequences include intense droughts, water scarcity, severe fires, rising sea levels, flooding, melting polar ice, catastrophic storms, and declining biodiversity.",
          "climate change, global warming, environment, sustainability, greenhouse gases",
          0.82,
          0));

      // Business Pages
      pages.add(createPage(
          "https://example.com/digital-marketing",
          "Digital Marketing Strategies",
          "Effective digital marketing techniques for modern businesses",
          "Digital marketing encompasses all marketing efforts that use an electronic device or the internet. Businesses leverage digital channels such as search engines, social media, email, and other websites to connect with current and prospective customers. This includes SEO (Search Engine Optimization), content marketing, social media marketing, pay-per-click advertising, affiliate marketing, and email marketing. The key advantage of digital marketing is the ability to measure and track results in real-time, allowing businesses to optimize their strategies for better ROI.",
          "digital marketing, seo, social media, advertising, business",
          0.87,
          0));

      pages.add(createPage(
          "https://example.com/entrepreneurship",
          "Starting Your Own Business",
          "Essential guide for aspiring entrepreneurs and startup founders",
          "Entrepreneurship is the process of designing, launching, and running a new business. Starting a business requires careful planning, market research, financial management, and dedication. Key steps include identifying a business opportunity, creating a business plan, securing funding, choosing a business structure, registering your business, and building a team. Successful entrepreneurs are innovative, risk-takers, and persistent. They understand their target market, adapt to changes, and continuously learn from failures and successes.",
          "entrepreneurship, startup, business, innovation, funding",
          0.79,
          0));

      // Health & Lifestyle
      pages.add(createPage(
          "https://example.com/healthy-living",
          "Guide to Healthy Living",
          "Tips and advice for maintaining a healthy lifestyle",
          "Healthy living involves making choices that improve your physical and mental well-being. This includes eating a balanced diet rich in fruits, vegetables, whole grains, and lean proteins. Regular physical activity, such as 150 minutes of moderate exercise per week, is essential. Getting adequate sleep (7-9 hours for adults), managing stress through meditation or yoga, staying hydrated, and avoiding harmful habits like smoking and excessive alcohol consumption are crucial. Regular health check-ups and maintaining social connections also contribute to overall wellness.",
          "health, lifestyle, wellness, fitness, nutrition",
          0.84,
          0));

      // Save all pages to database
      log.info("Saving {} pages to database...", pages.size());
      List<Page> savedPages = pageRepository.saveAll(pages);
      log.info("Successfully saved {} pages to database", savedPages.size());

      // Index pages in Elasticsearch
      log.info("Indexing pages in Elasticsearch...");
      indexingService.createIndex();
      indexingService.bulkIndexPages(savedPages);
      log.info("Data seeding completed successfully!");

      response.put("status", "success");
      response.put("message", "Successfully seeded " + savedPages.size() + " pages");
      response.put("pagesAdded", savedPages.size());

      return ResponseEntity.ok(response);

    } catch (Exception e) {
      log.error("Error during data seeding: {}", e.getMessage(), e);
      response.put("status", "error");
      response.put("message", "Error seeding data: " + e.getMessage());
      return ResponseEntity.status(500).body(response);
    }
  }

  @GetMapping("/stats")
  public ResponseEntity<Map<String, Object>> getStats() {
    Map<String, Object> stats = new HashMap<>();
    stats.put("totalPages", pageRepository.count());
    stats.put("indexExists", indexingService.indexExists());
    stats.put("indexedDocuments", indexingService.getTotalDocuments());
    return ResponseEntity.ok(stats);
  }

  private Page createPage(String url, String title, String description, String content,
      String keywords, Double pageRank, Integer crawlDepth) {
    Page page = new Page();
    page.setUrl(url);
    page.setTitle(title);
    page.setDescription(description);
    page.setContent(content);
    page.setKeywords(keywords);
    page.setLanguage("en");
    page.setPageRank(pageRank);
    page.setCrawlDepth(crawlDepth);
    page.setStatusCode(200);
    page.setResponseTime(150);
    page.setContentHash(generateHash(content));
    page.setLastCrawledAt(LocalDateTime.now());

    return page;
  }

  private String generateHash(String content) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(content.getBytes("UTF-8"));
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1)
          hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (Exception e) {
      return "";
    }
  }
}
