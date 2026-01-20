package com.Project.SpiderSync.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.Project.SpiderSync.Enteties.Page;
import com.Project.SpiderSync.search.PageDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

  private final ElasticsearchClient elasticsearchClient;

  @Value("${elasticsearch.index.pages:search_pages}")
  private String indexName;

  /**
   * Advanced search with BM25 ranking
   */
  @Cacheable(value = "search-results", key = "#query + '-' + #from + '-' + #size")
  public List<PageDocument> search(String query, int from, int size) {
    try {
      SearchResponse<PageDocument> response = elasticsearchClient.search(s -> s
          .index(indexName)
          .query(q -> q
              .multiMatch(m -> m
                  .query(query)
                  .fields("title^3", "description^2", "content", "keywords^2")
                  .fuzziness("AUTO")))
          .from(from)
          .size(size)
          .highlight(h -> h
              .fields("title", f -> f)
              .fields("content", f -> f.numberOfFragments(3))),
          PageDocument.class);

      return response.hits().hits().stream()
          .map(Hit::source)
          .collect(Collectors.toList());

    } catch (IOException e) {
      log.error("Error searching Elasticsearch: {}", e.getMessage());
      return new ArrayList<>();
    }
  }

  /**
     * Autocomplete suggestions
     */
    @Cacheable(value = "search-results", key = "'autocomplete-' + #prefix")
    public List<String> autocomplete(String prefix, int size) {
        try {
            SearchResponse<PageDocument> response = elasticsearchClient.search(s -> s
                    .index(indexName)
                    .query(q -> q
                        .matchPhrasePrefixquery -> query
                            .field("title")
                            .query(prefix)
                        )
                    )
                    .size(size)
                    .source(src -> src.filter(f -> f.includes("title"))),
                PageDocument.class
            );

            return response.hits().hits().stream()
                    .map(hit -> hit.source().getTitle())
                    .distinct()
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Error in autocomplete: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

  /**
   * Search with filters
   */
  public List<PageDocument> searchWithFilters(String query, String language, Double minPageRank, int from, int size) {
    try {
      List<Query> mustQueries = new ArrayList<>();

      // Main search query
      mustQueries.add(Query.of(q -> q
          .multiMatch(m -> m
              .query(query)
              .fields("title^3", "description^2", "content", "keywords^2"))));

      // Language filter
      if (language != null && !language.isEmpty()) {
        mustQueries.add(Query.of(q -> q
            .term(t -> t
                .field("language")
                .value(language))));
      }

      // Page rank filter
      if (minPageRank != null) {
        mustQueries.add(Query.of(q -> q
            .range(r -> r
                .field("page_rank")
                .gte(com.fasterxml.jackson.databind.JsonNode.valueOf(minPageRank)))));
      }

      SearchResponse<PageDocument> response = elasticsearchClient.search(s -> s
          .index(indexName)
          .query(q -> q
              .bool(b -> b.must(mustQueries)))
          .from(from)
          .size(size),
          PageDocument.class);

      return response.hits().hits().stream()
          .map(Hit::source)
          .collect(Collectors.toList());

    } catch (IOException e) {
      log.error("Error in filtered search: {}", e.getMessage());
      return new ArrayList<>();
    }
  }

  /**
   * Get total document count
   */
  public long getTotalDocuments() {
    try {
      CountResponse response = elasticsearchClient.count(c -> c.index(indexName));
      return response.count();
    } catch (IOException e) {
      log.error("Error counting documents: {}", e.getMessage());
      return 0;
    }
  }

  /**
   * Check if index exists
   */
  public boolean indexExists() {
    try {
      return elasticsearchClient.indices().exists(e -> e.index(indexName)).value();
    } catch (IOException e) {
      log.error("Error checking index existence: {}", e.getMessage());
      return false;
    }
  }
}
