package com.Project.SpiderSync.controller;

import com.Project.SpiderSync.dto.SearchRequestDTO;
import com.Project.SpiderSync.dto.SearchResponseDTO;
import com.Project.SpiderSync.entities.SearchQuery;
import com.Project.SpiderSync.repositories.SearchQueryRepository;
import com.Project.SpiderSync.search.PageDocument;
import com.Project.SpiderSync.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Advanced search endpoints")
public class SearchController {

  private final SearchService searchService;
  private final SearchQueryRepository searchQueryRepository;

  @GetMapping
  @Operation(summary = "Search pages", description = "Search indexed pages with optional filters")
  public SearchResponseDTO search(
      @RequestParam String q,
      @RequestParam(required = false) String language,
      @RequestParam(required = false) Double minPageRank,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      HttpServletRequest request) {
    long startTime = System.currentTimeMillis();

    List<PageDocument> results;
    if (language != null || minPageRank != null) {
      results = searchService.searchWithFilters(q, language, minPageRank, page * size, size);
    } else {
      results = searchService.search(q, page * size, size);
    }

    long searchTime = System.currentTimeMillis() - startTime;

    // Track search query
    trackSearchQuery(q, results.size(), request);

    SearchResponseDTO response = new SearchResponseDTO();
    response.setResults(results);
    response.setTotalResults(searchService.getTotalDocuments());
    response.setPage(page);
    response.setSize(size);
    response.setSearchTimeMs(searchTime);

    return response;
  }

  @GetMapping("/autocomplete")
  @Operation(summary = "Autocomplete suggestions", description = "Get search suggestions based on prefix")
  public List<String> autocomplete(
      @RequestParam String q,
      @RequestParam(defaultValue = "5") int size) {
    return searchService.autocomplete(q, size);
  }

  @GetMapping("/stats")
  @Operation(summary = "Search statistics", description = "Get total indexed documents count")
  public SearchStatsDTO getStats() {
    long totalDocs = searchService.getTotalDocuments();
    boolean indexExists = searchService.indexExists();

    SearchStatsDTO stats = new SearchStatsDTO();
    stats.setTotalDocuments(totalDocs);
    stats.setIndexExists(indexExists);
    stats.setIndexName("search_pages");

    return stats;
  }

  private void trackSearchQuery(String query, int resultsCount, HttpServletRequest request) {
    try {
      SearchQuery searchQuery = new SearchQuery();
      searchQuery.setQueryText(query);
      searchQuery.setResultsCount(resultsCount);
      searchQuery.setIpAddress(getClientIP(request));
      searchQuery.setUserAgent(request.getHeader("User-Agent"));
      searchQueryRepository.save(searchQuery);
    } catch (Exception e) {
      // Don't fail the search if tracking fails
    }
  }

  private String getClientIP(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
  }

  // Inner DTO class
  public static class SearchStatsDTO {
    private long totalDocuments;
    private boolean indexExists;
    private String indexName;

    public long getTotalDocuments() {
      return totalDocuments;
    }

    public void setTotalDocuments(long totalDocuments) {
      this.totalDocuments = totalDocuments;
    }

    public boolean isIndexExists() {
      return indexExists;
    }

    public void setIndexExists(boolean indexExists) {
      this.indexExists = indexExists;
    }

    public String getIndexName() {
      return indexName;
    }

    public void setIndexName(String indexName) {
      this.indexName = indexName;
    }
  }
}
