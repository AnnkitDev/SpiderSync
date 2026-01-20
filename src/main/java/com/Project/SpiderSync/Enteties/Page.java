package com.Project.SpiderSync.Enteties;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pages", indexes = {
    @Index(name = "idx_url", columnList = "url"),
    @Index(name = "idx_title", columnList = "title"),
    @Index(name = "idx_language", columnList = "language"),
    @Index(name = "idx_last_crawled", columnList = "lastCrawledAt"),
    @Index(name = "idx_page_rank", columnList = "pageRank")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}

