package com.Project.SpiderSync.repositories;

import com.Project.SpiderSync.entities.CrawlJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrawlJobRepository extends JpaRepository<CrawlJob, Long> {
    List<CrawlJob> findByStatusOrderByCreatedAtDesc(CrawlJob.CrawlStatus status);
    List<CrawlJob> findTop10ByOrderByCreatedAtDesc();
}
