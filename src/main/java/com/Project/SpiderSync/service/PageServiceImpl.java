package com.Project.SpiderSync.service;

import com.Project.SpiderSync.Enteties.Page;
import com.Project.SpiderSync.Repositories.PageRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private PageRepository pageRepository;

    // Memory to prevent the spider from going in circles
    private final Set<String> visitedUrls = new HashSet<>();

    @Override
    public void savePage(Page page) {
        // Check if DB already has it
        if (!pageRepository.existsByUrl(page.getUrl())) {
            pageRepository.save(page);
        } else {
            System.out.println("Skipping: URL already exists in database.");
        }
    }

    @Override
    public List<Page> searchPages(String query) {
        return pageRepository.search(query);
    }

    @Override
    public void startCrawl(String url) {
        // 1. Memory Check: Stop if we've been here in this session
        if (visitedUrls.contains(url)) {
            return;
        }

        try {
            visitedUrls.add(url);

            // 2. Connect and Download
            Document doc = Jsoup.connect(url).get();

            // 3. Extract
            String title = doc.title();
            String content = doc.body().text();

            // 4. Map and Save
            Page page = new Page();
            page.setUrl(url);
            page.setTitle(title); // Adding the title
            page.setContent(content);
            savePage(page);

            System.out.println("Indexed: " + title);

            // 5. Discovery: Find all links on the current page
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String nextUrl = link.attr("abs:href");

                // Only follow links that look like web addresses
                if (nextUrl.startsWith("http")) {
                    startCrawl(nextUrl);
                }
            }

        } catch (Exception e) {
            System.out.println("Could not crawl " + url + " - " + e.getMessage());
        }
    }
}