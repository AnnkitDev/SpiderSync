package com.Project.SpiderSync.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class PageDTO {
  private Long id;
  private String url;
  private String title;
  private String description;
  private String keywords;
  private String language;
  private Double pageRank;
  private Integer crawlDepth;
  private LocalDateTime lastCrawledAt;

  public PageDTO() {
  }

  public PageDTO(Long id, String url, String title, String description, String keywords, String language,
      Double pageRank, Integer crawlDepth, LocalDateTime lastCrawledAt) {
    this.id = id;
    this.url = url;
    this.title = title;
    this.description = description;
    this.keywords = keywords;
    this.language = language;
    this.pageRank = pageRank;
    this.crawlDepth = crawlDepth;
    this.lastCrawledAt = lastCrawledAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Double getPageRank() {
    return pageRank;
  }

  public void setPageRank(Double pageRank) {
    this.pageRank = pageRank;
  }

  public Integer getCrawlDepth() {
    return crawlDepth;
  }

  public void setCrawlDepth(Integer crawlDepth) {
    this.crawlDepth = crawlDepth;
  }

  public LocalDateTime getLastCrawledAt() {
    return lastCrawledAt;
  }

  public void setLastCrawledAt(LocalDateTime lastCrawledAt) {
    this.lastCrawledAt = lastCrawledAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    PageDTO pageDTO = (PageDTO) o;
    return Objects.equals(id, pageDTO.id) && Objects.equals(url, pageDTO.url) && Objects.equals(title, pageDTO.title)
        && Objects.equals(description, pageDTO.description) && Objects.equals(keywords, pageDTO.keywords)
        && Objects.equals(language, pageDTO.language) && Objects.equals(pageRank, pageDTO.pageRank)
        && Objects.equals(crawlDepth, pageDTO.crawlDepth) && Objects.equals(lastCrawledAt, pageDTO.lastCrawledAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, url, title, description, keywords, language, pageRank, crawlDepth, lastCrawledAt);
  }

  @Override
  public String toString() {
    return "PageDTO{" +
        "id=" + id +
        ", url='" + url + '\'' +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", keywords='" + keywords + '\'' +
        ", language='" + language + '\'' +
        ", pageRank=" + pageRank +
        ", crawlDepth=" + crawlDepth +
        ", lastCrawledAt=" + lastCrawledAt +
        '}';
  }
}
