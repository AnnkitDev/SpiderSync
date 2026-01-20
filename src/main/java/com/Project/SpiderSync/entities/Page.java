package com.Project.SpiderSync.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "pages", indexes = {
        @Index(name = "idx_url", columnList = "url"),
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_language", columnList = "language"),
        @Index(name = "idx_last_crawled", columnList = "lastCrawledAt"),
        @Index(name = "idx_page_rank", columnList = "pageRank")
})
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 768)
    private String url;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(length = 1000)
    private String description;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(length = 500)
    private String keywords;

    @Column(length = 10)
    private String language = "en";

    @Column(length = 64)
    private String contentHash;

    @Column
    private Integer statusCode = 200;

    @Column
    private Integer responseTime;

    @Column
    private Integer crawlDepth = 0;

    @Column
    private Double pageRank = 0.0;

    @Column
    private LocalDateTime lastCrawledAt;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public Page() {
    }

    public Page(Long id, String url, String title, String description, String content, String keywords, String language,
            String contentHash, Integer statusCode, Integer responseTime, Integer crawlDepth, Double pageRank,
            LocalDateTime lastCrawledAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.content = content;
        this.keywords = keywords;
        this.language = language;
        this.contentHash = contentHash;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
        this.crawlDepth = crawlDepth;
        this.pageRank = pageRank;
        this.lastCrawledAt = lastCrawledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getCrawlDepth() {
        return crawlDepth;
    }

    public void setCrawlDepth(Integer crawlDepth) {
        this.crawlDepth = crawlDepth;
    }

    public Double getPageRank() {
        return pageRank;
    }

    public void setPageRank(Double pageRank) {
        this.pageRank = pageRank;
    }

    public LocalDateTime getLastCrawledAt() {
        return lastCrawledAt;
    }

    public void setLastCrawledAt(LocalDateTime lastCrawledAt) {
        this.lastCrawledAt = lastCrawledAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        lastCrawledAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Page page = (Page) o;
        return Objects.equals(id, page.id) && Objects.equals(url, page.url) && Objects.equals(title, page.title)
                && Objects.equals(description, page.description) && Objects.equals(content, page.content)
                && Objects.equals(keywords, page.keywords) && Objects.equals(language, page.language)
                && Objects.equals(contentHash, page.contentHash) && Objects.equals(statusCode, page.statusCode)
                && Objects.equals(responseTime, page.responseTime) && Objects.equals(crawlDepth, page.crawlDepth)
                && Objects.equals(pageRank, page.pageRank) && Objects.equals(lastCrawledAt, page.lastCrawledAt)
                && Objects.equals(createdAt, page.createdAt) && Objects.equals(updatedAt, page.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, title, description, content, keywords, language, contentHash, statusCode,
                responseTime, crawlDepth, pageRank, lastCrawledAt, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Page{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", keywords='" + keywords + '\'' +
                ", language='" + language + '\'' +
                ", statusCode=" + statusCode +
                ", responseTime=" + responseTime +
                ", crawlDepth=" + crawlDepth +
                ", pageRank=" + pageRank +
                ", lastCrawledAt=" + lastCrawledAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
