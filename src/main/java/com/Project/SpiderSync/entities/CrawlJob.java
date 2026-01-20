package com.Project.SpiderSync.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "crawl_jobs", indexes = {
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "createdAt")
})
public class CrawlJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 768)
    private String seedUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CrawlStatus status = CrawlStatus.PENDING;

    @Column
    private Integer pagesCrawled = 0;

    @Column
    private Integer pagesIndexed = 0;

    @Column
    private Integer errorsCount = 0;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    public CrawlJob() {
    }

    public CrawlJob(Long id, String seedUrl, CrawlStatus status, Integer pagesCrawled, Integer pagesIndexed,
            Integer errorsCount, LocalDateTime startedAt, LocalDateTime completedAt, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.seedUrl = seedUrl;
        this.status = status;
        this.pagesCrawled = pagesCrawled;
        this.pagesIndexed = pagesIndexed;
        this.errorsCount = errorsCount;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeedUrl() {
        return seedUrl;
    }

    public void setSeedUrl(String seedUrl) {
        this.seedUrl = seedUrl;
    }

    public CrawlStatus getStatus() {
        return status;
    }

    public void setStatus(CrawlStatus status) {
        this.status = status;
    }

    public Integer getPagesCrawled() {
        return pagesCrawled;
    }

    public void setPagesCrawled(Integer pagesCrawled) {
        this.pagesCrawled = pagesCrawled;
    }

    public Integer getPagesIndexed() {
        return pagesIndexed;
    }

    public void setPagesIndexed(Integer pagesIndexed) {
        this.pagesIndexed = pagesIndexed;
    }

    public Integer getErrorsCount() {
        return errorsCount;
    }

    public void setErrorsCount(Integer errorsCount) {
        this.errorsCount = errorsCount;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
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
        CrawlJob crawlJob = (CrawlJob) o;
        return Objects.equals(id, crawlJob.id) && Objects.equals(seedUrl, crawlJob.seedUrl) && status == crawlJob.status
                && Objects.equals(pagesCrawled, crawlJob.pagesCrawled)
                && Objects.equals(pagesIndexed, crawlJob.pagesIndexed)
                && Objects.equals(errorsCount, crawlJob.errorsCount) && Objects.equals(startedAt, crawlJob.startedAt)
                && Objects.equals(completedAt, crawlJob.completedAt) && Objects.equals(createdAt, crawlJob.createdAt)
                && Objects.equals(updatedAt, crawlJob.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seedUrl, status, pagesCrawled, pagesIndexed, errorsCount, startedAt, completedAt,
                createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "CrawlJob{" +
                "id=" + id +
                ", seedUrl='" + seedUrl + '\'' +
                ", status=" + status +
                ", pagesCrawled=" + pagesCrawled +
                ", pagesIndexed=" + pagesIndexed +
                ", errorsCount=" + errorsCount +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public enum CrawlStatus {
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED,
        CANCELLED
    }
}
