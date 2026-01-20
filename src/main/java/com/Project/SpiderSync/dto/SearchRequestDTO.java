package com.Project.SpiderSync.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDTO {
  private String query;
  private String language;
  private Double minPageRank;
  private Integer page = 0;
  private Integer size = 10;
}
