package com.Project.SpiderSync.Repositories;

import com.Project.SpiderSync.Enteties.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public  interface PageRepository extends JpaRepository<Page, Long> {

    @Query(value = "SELECT * FROM pages WHERE MATCH(content) AGAINST (:keyword IN NATURAL LANGUAGE MODE",
    nativeQuery = true)
    List<Page> search(@Param("Keyword") String keyword);


    boolean existByUrl(String url);

    }

