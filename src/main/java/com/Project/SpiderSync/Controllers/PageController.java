package com.Project.SpiderSync.Controllers;

import com.Project.SpiderSync.Enteties.Page;
import com.Project.SpiderSync.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pages")
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("/crawl")
   public String startCrawl(@RequestParam String url){
       pageService.startCrawl(url);
       return "Crawl started for: "+url;
   }


   @GetMapping("/search")
   public List<Page> search(@RequestParam String query){
        return pageService.searchPages(query);
   }
}
