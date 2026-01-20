package com.Project.SpiderSync.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import com.Project.SpiderSync.entities.Page;
import com.Project.SpiderSync.search.PageDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class IndexingService {

  private static final Logger log = LoggerFactory.getLogger(IndexingService.class);

  private final ElasticsearchClient elasticsearchClient;

  public IndexingService(ElasticsearchClient elasticsearchClient) {
    this.elasticsearchClient = elasticsearchClient;
  }

  @Value("${elasticsearch.index.pages:search_pages}")
  private String indexName;

  /**
   * Create index with optimized mappings
   */
  public void createIndex() {
    try {
      boolean exists = elasticsearchClient.indices()
          .exists(ExistsRequest.of(e -> e.index(indexName)))
          .value();

      if (!exists) {
        elasticsearchClient.indices().create(c -> c
            .index(indexName)
            .mappings(m -> m
                .properties("id", Property.of(p -> p.long_(l -> l)))
                .properties("url", Property.of(p -> p.keyword(k -> k)))
                .properties("title", Property.of(p -> p.text(t -> t
                    .analyzer("standard")
                    .fields("keyword", Property.of(f -> f.keyword(k -> k))))))
                .properties("description", Property.of(p -> p.text(t -> t.analyzer("standard"))))
                .properties("content", Property.of(p -> p.text(t -> t.analyzer("standard"))))
                .properties("keywords", Property.of(p -> p.text(t -> t.analyzer("standard"))))
                .properties("language", Property.of(p -> p.keyword(k -> k)))
                .properties("page_rank", Property.of(p -> p.double_(d -> d)))
                .properties("crawl_depth", Property.of(p -> p.integer(i -> i)))
                .properties("last_crawled_at", Property.of(p -> p.date(d -> d))))
            .settings(s -> s
                .numberOfShards("1")
                .numberOfReplicas("0")));

        log.info("Created Elasticsearch index: {}", indexName);
      } else {
        log.info("Elasticsearch index already exists: {}", indexName);
      }
    } catch (IOException e) {
      log.error("Error creating Elasticsearch index: {}", e.getMessage());
    }
  }

  /**
   * Index a single page
   */
  public void indexPage(Page page) {
    try {
      PageDocument doc = convertToDocument(page);

      IndexResponse response = elasticsearchClient.index(i -> i
          .index(indexName)
          .id(page.getId().toString())
          .document(doc));

      log.debug("Indexed page: {} with result: {}", page.getUrl(), response.result());
    } catch (IOException e) {
      log.error("Error indexing page {}: {}", page.getUrl(), e.getMessage());
    }
  }

  /**
   * Bulk index multiple pages
   */
  public void bulkIndexPages(List<Page> pages) {
    if (pages.isEmpty()) {
      return;
    }

    try {
      BulkRequest.Builder br = new BulkRequest.Builder();

      for (Page page : pages) {
        PageDocument doc = convertToDocument(page);
        br.operations(op -> op
            .index(idx -> idx
                .index(indexName)
                .id(page.getId().toString())
                .document(doc)));
      }

      BulkResponse result = elasticsearchClient.bulk(br.build());

      if (result.errors()) {
        log.error("Bulk indexing had errors");
        for (BulkResponseItem item : result.items()) {
          if (item.error() != null) {
            log.error("Error indexing document {}: {}", item.id(), item.error().reason());
          }
        }
      } else {
        log.info("Successfully bulk indexed {} pages", pages.size());
      }
    } catch (IOException e) {
      log.error("Error in bulk indexing: {}", e.getMessage());
    }
  }

  /**
   * Delete a page from index
   */
  public void deletePage(Long pageId) {
    try {
      elasticsearchClient.delete(d -> d
          .index(indexName)
          .id(pageId.toString()));
      log.debug("Deleted page from index: {}", pageId);
    } catch (IOException e) {
      log.error("Error deleting page {}: {}", pageId, e.getMessage());
    }
  }

  /**
   * Re-index all pages
   */
  public void reindexAll(List<Page> allPages) {
    log.info("Starting reindex of {} pages", allPages.size());

    // Delete existing index
    try {
      elasticsearchClient.indices().delete(d -> d.index(indexName));
    } catch (IOException e) {
      log.warn("Could not delete index (may not exist): {}", e.getMessage());
    }

    // Create new index
    createIndex();

    // Bulk index all pages
    int batchSize = 100;
    for (int i = 0; i < allPages.size(); i += batchSize) {
      int end = Math.min(i + batchSize, allPages.size());
      List<Page> batch = allPages.subList(i, end);
      bulkIndexPages(batch);
    }

    log.info("Reindexing completed");
  }

  /**
   * Convert Page entity to PageDocument
   */
  private PageDocument convertToDocument(Page page) {
    PageDocument doc = new PageDocument();
    doc.setId(page.getId());
    doc.setUrl(page.getUrl());
    doc.setTitle(page.getTitle());
    doc.setDescription(page.getDescription());
    doc.setContent(page.getContent());
    doc.setKeywords(page.getKeywords());
    doc.setLanguage(page.getLanguage());
    doc.setPageRank(page.getPageRank());
    doc.setCrawlDepth(page.getCrawlDepth());

    if (page.getLastCrawledAt() != null) {
      doc.setLastCrawledAt(page.getLastCrawledAt().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    return doc;
  }
}
