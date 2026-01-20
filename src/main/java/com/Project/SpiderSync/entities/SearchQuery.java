package com.Project.SpiderSync.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "search_queries", indexes = {
        @Index(name = "idx_query_text", columnList = "queryText"),
        @Index(name = "idx_created_at", columnList = "createdAt"),
        @Index(name = "idx_user_id", columnList = "userId")
})
public class SearchQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String queryText;

    @Column
    private Integer resultsCount = 0;

    @Column
    private Long userId;

    @Column(length = 45)
    private String ipAddress;

    @Column(length = 500)
    private String userAgent;

    @Column
    private Long clickedResultId;

    @Column
    private Integer clickPosition;

    @Column
    private Integer searchTimeMs;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public SearchQuery() {
    }

    public SearchQuery(Long id, String queryText, Integer resultsCount, Long userId, String ipAddress, String userAgent,
            Long clickedResultId, Integer clickPosition, Integer searchTimeMs, LocalDateTime createdAt) {
        this.id = id;
        this.queryText = queryText;
        this.resultsCount = resultsCount;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.clickedResultId = clickedResultId;
        this.clickPosition = clickPosition;
        this.searchTimeMs = searchTimeMs;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public Integer getResultsCount() {
        return resultsCount;
    }

    public void setResultsCount(Integer resultsCount) {
        this.resultsCount = resultsCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getClickedResultId() {
        return clickedResultId;
    }

    public void setClickedResultId(Long clickedResultId) {
        this.clickedResultId = clickedResultId;
    }

    public Integer getClickPosition() {
        return clickPosition;
    }

    public void setClickPosition(Integer clickPosition) {
        this.clickPosition = clickPosition;
    }

    public Integer getSearchTimeMs() {
        return searchTimeMs;
    }

    public void setSearchTimeMs(Integer searchTimeMs) {
        this.searchTimeMs = searchTimeMs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SearchQuery that = (SearchQuery) o;
        return Objects.equals(id, that.id) && Objects.equals(queryText, that.queryText)
                && Objects.equals(resultsCount, that.resultsCount) && Objects.equals(userId, that.userId)
                && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(userAgent, that.userAgent)
                && Objects.equals(clickedResultId, that.clickedResultId)
                && Objects.equals(clickPosition, that.clickPosition) && Objects.equals(searchTimeMs, that.searchTimeMs)
                && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, queryText, resultsCount, userId, ipAddress, userAgent, clickedResultId, clickPosition,
                searchTimeMs, createdAt);
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
                "id=" + id +
                ", queryText='" + queryText + '\'' +
                ", resultsCount=" + resultsCount +
                ", userId=" + userId +
                ", ipAddress='" + ipAddress + '\'' +
                ", searchTimeMs=" + searchTimeMs +
                ", createdAt=" + createdAt +
                '}';
    }
}
