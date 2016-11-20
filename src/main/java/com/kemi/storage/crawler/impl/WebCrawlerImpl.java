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
                || link.startsWith("ekmair.ukma.edu.ua")
                || link.startsWith("http://kpi.ua/")
                || link.startsWith("http://kpi.ua")
                || link.startsWith("kpi.ua")
                || link.startsWith("http://bulletin.kpi.ua/")
                || link.startsWith("http://bulletin.kpi.ua")
                || link.startsWith("bulletin.kpi.ua")
                || link.startsWith("http://old.bulletin.kpi.ua/")
                || link.startsWith("http://old.bulletin.kpi.ua")
                || link.startsWith("old.bulletin.kpi.ua")
                || link.startsWith("http://chemengine.kpi.ua/")
                || link.startsWith("http://chemengine.kpi.ua")
                || link.startsWith("chemengine.kpi.ua")
                || link.startsWith("http://historypages.kpi.ua")
                || link.startsWith("http://history-pages.kpi.ua")
                || link.startsWith("http://visnyk-psp.kpi.ua")
                || link.startsWith("http://journal-phipsyped.kpi.ua")
                || link.startsWith("http://novyn.kpi.ua")
                || link.startsWith("http://mining.kpi.ua")
                || link.startsWith("http://ev.fmm.kpi.ua")
                || link.startsWith("http://economy.kpi.ua")
                || link.startsWith("http://ape.fmm.kpi.ua")
                || link.startsWith("http://probl-economy.kpi.ua")
                || link.startsWith("http://sb-keip.kpi.ua")
                || link.startsWith("http://mgsys.kpi.ua")
                || link.startsWith("http://ismc.kpi.ua")
                || link.startsWith("http://journal.mmi.kpi.ua")
                || link.startsWith("http://visnyk-mmi.kpi.ua")
                || link.startsWith("http://visnykpb.kpi.ua")
                || link.startsWith("http://it-visnyk.kpi.ua")
                || link.startsWith("http://ttdruk.vpi.kpi.ua")
                || link.startsWith("http://druk.kpi.ua")
                || link.startsWith("http://ae.fl.kpi.ua")
                || link.startsWith("http://visnyk.fl.kpi.ua")
                || link.startsWith("http://rht-journal.kpi.ua")
                || link.startsWith("http://energy.kpi.ua")
                || link.startsWith("http://its.iszzi.kpi.ua")
                || link.startsWith("http://journal.iasa.kpi.ua")
                || link.startsWith("http://infotelesc.kpi.ua")
                || link.startsWith("http://asac.kpi.ua")
                || link.startsWith("http://pnzzi.kpi.ua")
                || link.startsWith("http://radap.kpi.ua")
                || link.startsWith("http://elc.kpi.ua"))
        && !unavailableLinks.contains(link);
    }

    private void getLinksOnPage(String url) {
        if (visitedLinks.contains(url))
            return;
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
        /*for (String link : references)
            getLinksOnPage(link);*/
    }

    private boolean parsePage(String url, List<String> references) {
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(timeout).get();
            for (Element link : doc.select("a[href]")) {
                String href = link.attr("href");
                if (visitedLinks.contains(url))
                    continue;
                href = format(href, url);
                if (visitedLinks.contains(url))
                    continue;
                if (!isAllowed(href))
                    continue;
                if (href.contains(".pdf")) {
                    href = href.substring(0, href.lastIndexOf(".pdf")+4);
                    System.out.println("New pdf is "+href);
                    pdfLinks.add(href);
                    visitedLinks.add(href);
                    linksDao.create(href);
                } else
                if (href.contains(".PDF")) {
                    href = href.substring(0, href.lastIndexOf(".PDF")+4);
                    System.out.println("New pdf is "+href);
                    pdfLinks.add(href);
                    visitedLinks.add(href);
                    linksDao.create(href);
                }  else
                if (href.contains("download")) {
                    System.out.println("New pdf is "+href);
                    pdfLinks.add(href);
                    visitedLinks.add(href);
                    linksDao.create(href);
                } else
                    references.add(href);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String format(String href, String url) {
        if(href.startsWith("#") || href.startsWith("mailto"))
            return "";
        int dot = url.indexOf('.');
        int slh = url.indexOf( '/', dot);
        String place = url.substring(0, slh > 0 ? slh : url.length());
        if (!href.startsWith("http"))
            href = place + href;
        return href;
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