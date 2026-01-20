package com.Project.SpiderSync.Controllers;

import com.Project.SpiderSync.Enteties.Page;
import com.Project.SpiderSync.service.PageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pages")
@Tag(name = "Pages", description = "Page management and crawling endpoints")
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("/crawl")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Start crawling", description = "Start crawling from a seed URL (Admin only)")
    public ResponseEntity<Map<String, String>> startCrawl(@RequestParam String url) {
        pageService.startCrawl(url);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Crawl started for: " + url);
        response.put("status", "INITIATED");
        response.put("url", url);

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search pages (MySQL)", description = "Search pages using MySQL full-text search (fallback)")
    public List<Page> search(@RequestParam String query) {
        return pageService.searchPages(query);
    }
}
