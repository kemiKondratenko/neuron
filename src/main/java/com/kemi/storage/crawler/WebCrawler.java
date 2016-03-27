package com.kemi.storage.crawler;

import java.net.URL;
import java.util.HashSet;

/**
 * Created by Kutsyk on 21.03.15.
 */
public interface WebCrawler {
    void start(URL url, int time) throws Exception;

    HashSet<String> getVisitedLinks();

    HashSet<String> getPdfLinks();
}