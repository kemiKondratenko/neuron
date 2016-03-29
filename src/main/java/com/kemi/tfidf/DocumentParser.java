package com.kemi.tfidf;

import com.google.common.collect.Lists;
import com.kemi.database.EntitiesDao;
import com.kemi.database.WordDao;
import com.kemi.entities.PdfLink;
import com.kemi.service.text.load.Loader;
import com.kemi.entities.WordEntity;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.BreakIterator;
import java.util.Collection;
import java.util.List;

/**
 * Created by Eugene on 23.03.2016.
 */
@Service
public class DocumentParser {

    @Autowired
    private EntitiesDao entitiesDao;
    @Autowired
    private Loader loader;
    @Autowired
    private WordDao wordDao;

    public void parseFiles() {
        List<PdfLink> links = entitiesDao.get(PdfLink.class, Restrictions.isEmpty("linkToUdcs"));
        buildWordEntities(links);
    }

    private void buildWordEntities(List<PdfLink> links) {
        for (PdfLink link : links) {
            String text = loader.loadText(link.getPdfLink());
            parseAndBuildWords(link, text);
        }
    }

    private void parseAndBuildWords(PdfLink link, String text) {
        for (String s : text.split("\\w+")) {
            WordEntity word = wordDao.create(s);
        }
    }

    private Collection<String> getWordsAsString(String source) {
        return get(source, BreakIterator.getWordInstance());
    }

    private List<String> get(String source, BreakIterator iterator) {
        List<String> res = Lists.newArrayList();
        iterator.setText(source);
        int start = iterator.first();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
            res.add(source.substring(start,end));
        }
        return res;
    }
}
