package com.Project.SpiderSync.dto;

import com.Project.SpiderSync.search.PageDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDTO {
  private List<PageDocument> results;
  private long totalResults;
  private int page;
  private int size;
  private long searchTimeMs;
}
