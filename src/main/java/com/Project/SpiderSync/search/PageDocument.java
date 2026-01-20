package com.Project.SpiderSync.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
