package com.Project.SpiderSync.repositories;

import com.Project.SpiderSync.entities.SearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long> {

  @Query("SELECT sq.queryText, COUNT(sq) as count FROM SearchQuery sq " +
      "WHERE sq.createdAt >= :since " +
      "GROUP BY sq.queryText " +
      "ORDER BY count DESC")
  List<Object[]> findTopQueries(LocalDateTime since);

  List<SearchQuery> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
