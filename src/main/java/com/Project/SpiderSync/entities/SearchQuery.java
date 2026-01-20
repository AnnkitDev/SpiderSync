package com.Project.SpiderSync.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_queries", indexes = {
    @Index(name = "idx_query_text", columnList = "queryText"),
    @Index(name = "idx_created_at", columnList = "createdAt"),
    @Index(name = "idx_user_id", columnList = "userId")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
