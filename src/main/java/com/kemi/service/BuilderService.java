package com.kemi.service;

import com.google.common.collect.Lists;
import com.kemi.database.EntitiesDao;
import com.kemi.entities.PdfLink;
import com.kemi.service.factory.Factory;
import com.kemi.service.text.load.Loader;
import com.kemi.storage.crawler.WebCrawler;
import com.kemi.system.Sentence;
import com.kemi.system.Word;
import com.kemi.udc.UdcFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URL;
import java.text.BreakIterator;
import java.util.Collection;
import java.util.List;

/**
 * Created by Eugene on 16.03.2016.
 */
@Service
public class BuilderService {

    @Autowired
    private Loader loader;
    @Autowired
    private Factory factory;
    @Autowired
    private WebCrawler webCrawler;
    @Autowired
    private UdcFinder udcFinder;

    @Autowired
    private EntitiesDao entitiesDao;

    public Collection<String> get() {
        try {
            webCrawler.start(new URL("http://nz.ukma.edu.ua/"), 6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //build(loader.loadText("/texts/n.txt"));
        List<String> re = Lists.newArrayList();
        re.addAll(webCrawler.getPdfLinks());
        re.add(webCrawler.getPdfLinks().size() + "");
        return re;//factory.getSentences();
    }

    private Collection<Sentence> build(String text) {
        return getSentences(text);
    }

    private Collection<Sentence> getSentences(String source) {
        List<Sentence> sentencesRes = Lists.newArrayList();
        Collection<String> sentences = getSentencesAsString(source);
        for (String sentence : sentences) {
            sentencesRes.add(factory.sentence(sentence, getWords(sentence)));
        }
        return sentencesRes;
    }

    private List<Word> getWords(String sentence) {
        List<Word> words = Lists.newArrayList();
        for (String word : getWordsAsString(sentence)) {
            words.add(factory.neuron(word));
        }
        return words;
    }

    private Collection<String> getWordsAsString(String source) {
        return get(source, BreakIterator.getWordInstance());
    }

    private Collection<String> getSentencesAsString(String source) {
        return get(source, BreakIterator.getSentenceInstance());
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

    @Transactional
    public Collection<PdfLink> find() {
        return entitiesDao.get(PdfLink.class);
    }

    @Transactional
    public Number count() {
        return entitiesDao.count(PdfLink.class);
    }

    public String findUdc() {
        return null;
    }
}
