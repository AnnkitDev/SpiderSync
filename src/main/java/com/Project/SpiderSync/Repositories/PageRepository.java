package com.Project.SpiderSync.repositories;

import com.Project.SpiderSync.entities.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    @Query(value = "SELECT * FROM pages WHERE MATCH(content) AGAINST (:keyword IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    List<Page> search(@Param("keyword") String keyword);

    boolean existsByUrl(String url);

    List<Page> findByUrlIn(List<String> urls);
}
