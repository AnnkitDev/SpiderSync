package com.Project.SpiderSync.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class PageDocument {

    private Long id;
    private String url;
    private String title;
    private String description;
    private String content;
    private String keywords;
    private String language;

    @JsonProperty("page_rank")
    private Double pageRank;

    @JsonProperty("crawl_depth")
    private Integer crawlDepth;

    @JsonProperty("last_crawled_at")
    private String lastCrawledAt;

    public PageDocument() {
    }

    public PageDocument(Long id, String url, String title, String description, String content, String keywords,
            String language, Double pageRank, Integer crawlDepth, String lastCrawledAt) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.content = content;
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

    public String getLastCrawledAt() {
        return lastCrawledAt;
    }

    public void setLastCrawledAt(String lastCrawledAt) {
        this.lastCrawledAt = lastCrawledAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PageDocument that = (PageDocument) o;
        return Objects.equals(id, that.id) && Objects.equals(url, that.url) && Objects.equals(title, that.title)
                && Objects.equals(description, that.description) && Objects.equals(content, that.content)
                && Objects.equals(keywords, that.keywords) && Objects.equals(language, that.language)
                && Objects.equals(pageRank, that.pageRank) && Objects.equals(crawlDepth, that.crawlDepth)
                && Objects.equals(lastCrawledAt, that.lastCrawledAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, title, description, content, keywords, language, pageRank, crawlDepth,
                lastCrawledAt);
    }

    @Override
    public String toString() {
        return "PageDocument{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", keywords='" + keywords + '\'' +
                ", language='" + language + '\'' +
                ", pageRank=" + pageRank +
                ", crawl_depth=" + crawlDepth +
                ", last_crawled_at='" + lastCrawledAt + '\'' +
                '}';
    }
}
