package com.Project.SpiderSync.service;

import com.Project.SpiderSync.Enteties.Page;

import java.util.List;

public interface PageService {

    void savePage(Page page);
    List<Page> searchPages(String query);
    void startCrawl(String url);
}
