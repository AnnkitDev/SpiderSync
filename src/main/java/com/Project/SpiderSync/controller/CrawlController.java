package com.Project.SpiderSync.controller;

import com.Project.SpiderSync.entities.CrawlJob;
import com.Project.SpiderSync.repositories.CrawlJobRepository;
import com.Project.SpiderSync.service.IndexingService;
import com.Project.SpiderSync.service.PageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crawl")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Crawl Management", description = "Crawl job management endpoints (Admin only)")
public class CrawlController {

  private final PageService pageService;
  private final CrawlJobRepository crawlJobRepository;
  private final IndexingService indexingService;

  public CrawlController(PageService pageService, CrawlJobRepository crawlJobRepository,
      IndexingService indexingService) {
    this.pageService = pageService;
    this.crawlJobRepository = crawlJobRepository;
    this.indexingService = indexingService;
  }

  @PostMapping("/start")
  @Operation(summary = "Start new crawl job", description = "Initiate a new crawl job from seed URL")
  public ResponseEntity<CrawlJob> startCrawl(@RequestParam String seedUrl) {
    CrawlJob job = new CrawlJob();
    job.setSeedUrl(seedUrl);
    job.setStatus(CrawlJob.CrawlStatus.RUNNING);
    job.setStartedAt(LocalDateTime.now());

    CrawlJob savedJob = crawlJobRepository.save(job);

    // Start crawling asynchronously
    pageService.startCrawl(seedUrl);

    return ResponseEntity.ok(savedJob);
  }

  @GetMapping("/jobs")
  @Operation(summary = "List crawl jobs", description = "Get list of recent crawl jobs")
  public List<CrawlJob> listJobs() {
    return crawlJobRepository.findTop10ByOrderByCreatedAtDesc();
  }

  @GetMapping("/jobs/{id}")
  @Operation(summary = "Get crawl job", description = "Get details of a specific crawl job")
  public ResponseEntity<CrawlJob> getJob(@PathVariable Long id) {
    return crawlJobRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/index/create")
  @Operation(summary = "Create Elasticsearch index", description = "Create Elasticsearch index with mappings")
  public ResponseEntity<Map<String, String>> createIndex() {
    indexingService.createIndex();

    Map<String, String> response = new HashMap<>();
    response.put("message", "Elasticsearch index created successfully");
    response.put("status", "SUCCESS");

    return ResponseEntity.ok(response);
  }

  @PostMapping("/index/reindex")
  @Operation(summary = "Reindex all pages", description = "Reindex all pages from MySQL to Elasticsearch")
  public ResponseEntity<Map<String, String>> reindexAll() {
    // This would need to be implemented with pagination for large datasets
    Map<String, String> response = new HashMap<>();
    response.put("message", "Reindexing initiated");
    response.put("status", "INITIATED");

    return ResponseEntity.accepted().body(response);
  }
}
