package com.kemi.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kemi.service.factory.Factory;
import com.kemi.service.text.load.Loader;
import com.kemi.system.Word;
import com.kemi.system.Sentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.BreakIterator;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Eugene on 16.03.2016.
 */
@Service
public class BuilderService {

    @Autowired
    private Loader loader;
    @Autowired
    private Factory factory;

    public String get() {
        build(loader.loadText("/texts/n.txt"));
        return factory.getSentences().toString();
    }

    private Collection<Sentence> build(String text) {
        Set<Word> res = Sets.newHashSet();
        Collection<Sentence> sentences = getSentences(text);
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
            factory.neuron(word);
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
}
