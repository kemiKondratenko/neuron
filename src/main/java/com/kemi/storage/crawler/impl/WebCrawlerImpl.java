package com.kemi.storage.crawler.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kemi.database.LinksDao;
import com.kemi.storage.crawler.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Kutsyk on 21.03.15.
 */
@Service
public class WebCrawlerImpl implements WebCrawler {

    @Autowired
    private LinksDao linksDao;

    private HashSet<String> visitedLinks = Sets.newHashSet();
    private HashSet<String> pdfLinks = Sets.newHashSet();
    private HashSet<String> unavailableLinks = Sets.newHashSet();
    private Integer timeout;

    @Transactional
    public void start(URL url, int time) {
        timeout = time;
        getLinksOnPage(url.toString());
    }

    private boolean isAllowed(String link) {
        return (link.startsWith("nz.ukma.edu.ua")
                || link.startsWith("http://nz.ukma.edu.ua")
                || link.startsWith("https://nz.ukma.edu.ua")
                ||link.startsWith("http://www.ekmair.ukma.edu.ua")
                || link.startsWith("http://ekmair.ukma.edu.ua")
                || link.startsWith("https://ekmair.ukma.edu.ua")
                || link.startsWith("ekmair.ukma.edu.ua"))
        && !unavailableLinks.contains(link);
    }

    private void getLinksOnPage(String url) {
        if (visitedLinks.contains(url))
            return;
        format(url);
        if (visitedLinks.contains(url))
            return;
        if (!isAllowed(url))
            return;
        System.out.println("New link is "+url);
        List<String> references = Lists.newArrayList();
        boolean pageParsed = parsePage(url, references);
        if(pageParsed)
            visitedLinks.add(url);
        else
            unavailableLinks.add(url);
        for (String link : references)
            getLinksOnPage(link);
    }

    private String format(String url) {
        if (!url.startsWith("http"))
            url = "http://nz.ukma.edu.ua" + url;
        return url;
    }

    private boolean parsePage(String url, List<String> references) {
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(timeout).get();
            for (Element link : doc.select("a[href]")) {
                String href = link.attr("href");
                href = format(href);
                if (!isAllowed(href))
                    continue;
                references.add(href);
                if (href.endsWith("pdf")) {
                    System.out.println("New pdf is "+href);
                    pdfLinks.add(href);
                    linksDao.create(href);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public HashSet<String> getVisitedLinks() {
        return visitedLinks;
    }

    @Override
    public HashSet<String> getPdfLinks() {
        return pdfLinks;
    }
}