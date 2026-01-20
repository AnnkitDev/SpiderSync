package com.Project.SpiderSync.dto;

import java.util.Objects;

public class SearchRequestDTO {
  private String query;
  private String language;
  private Double minPageRank;
  private Integer page = 0;
  private Integer size = 10;

  public SearchRequestDTO() {
  }

  public SearchRequestDTO(String query, String language, Double minPageRank, Integer page, Integer size) {
    this.query = query;
    this.language = language;
    this.minPageRank = minPageRank;
    this.page = page;
    this.size = size;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Double getMinPageRank() {
    return minPageRank;
  }

  public void setMinPageRank(Double minPageRank) {
    this.minPageRank = minPageRank;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    SearchRequestDTO that = (SearchRequestDTO) o;
    return Objects.equals(query, that.query) && Objects.equals(language, that.language)
        && Objects.equals(minPageRank, that.minPageRank) && Objects.equals(page, that.page)
        && Objects.equals(size, that.size);
  }

  @Override
  public int hashCode() {
    return Objects.hash(query, language, minPageRank, page, size);
  }

  @Override
  public String toString() {
    return "SearchRequestDTO{" +
        "query='" + query + '\'' +
        ", language='" + language + '\'' +
        ", minPageRank=" + minPageRank +
        ", page=" + page +
        ", size=" + size +
        '}';
  }
}
