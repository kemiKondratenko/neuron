package com.kemi.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.kemi.service.factory.Factory;
import com.kemi.service.text.load.Loader;
import com.kemi.system.Neuron;
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
        String text = loader.loadText("/texts/n.txt");
        Collection<Neuron> res = build(text);
        return res.toString();
    }

    private Collection<Sentence> build(String text) {
        Set<Neuron> res = Sets.newHashSet();
        Collection<Sentence> sentences = getSentences(text);
        Collection<String> words = getWords(sentences.get(0));
        for (String word : words) {
            res.add(factory.neuron(word));
        }
        return getSentences(text);
    }

    private Collection<String> getWords(String source) {
        return get(source, BreakIterator.getWordInstance());
    }

    private Collection<Sentence> getSentences(String source) {
        Collection<Sentence> sentences = getSentences(text);
        return factory.sentences(get(source, BreakIterator.getSentenceInstance()));
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
