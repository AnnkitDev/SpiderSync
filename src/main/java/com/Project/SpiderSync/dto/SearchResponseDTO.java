package com.Project.SpiderSync.dto;

import com.Project.SpiderSync.search.PageDocument;
import java.util.List;
import java.util.Objects;

public class SearchResponseDTO {
  private List<PageDocument> results;
  private long totalResults;
  private int page;
  private int size;
  private long searchTimeMs;

  public SearchResponseDTO() {
  }

  public SearchResponseDTO(List<PageDocument> results, long totalResults, int page, int size, long searchTimeMs) {
    this.results = results;
    this.totalResults = totalResults;
    this.page = page;
    this.size = size;
    this.searchTimeMs = searchTimeMs;
  }

  public List<PageDocument> getResults() {
    return results;
  }

  public void setResults(List<PageDocument> results) {
    this.results = results;
  }

  public long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(long totalResults) {
    this.totalResults = totalResults;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public long getSearchTimeMs() {
    return searchTimeMs;
  }

  public void setSearchTimeMs(long searchTimeMs) {
    this.searchTimeMs = searchTimeMs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    SearchResponseDTO that = (SearchResponseDTO) o;
    return totalResults == that.totalResults && page == that.page && size == that.size
        && searchTimeMs == that.searchTimeMs && Objects.equals(results, that.results);
  }

  @Override
  public int hashCode() {
    return Objects.hash(results, totalResults, page, size, searchTimeMs);
  }

  @Override
  public String toString() {
    return "SearchResponseDTO{" +
        "results=" + results +
        ", totalResults=" + totalResults +
        ", page=" + page +
        ", size=" + size +
        ", searchTimeMs=" + searchTimeMs +
        '}';
  }
}
