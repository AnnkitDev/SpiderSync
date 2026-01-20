package com.Project.SpiderSync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
