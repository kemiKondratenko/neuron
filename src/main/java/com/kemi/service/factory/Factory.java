package com.kemi.service.factory;

import com.google.common.collect.Maps;
import com.kemi.system.Word;
import com.kemi.system.Sentence;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Eugene on 17.03.2016.
 */
@Service
public class Factory {

    private Map<String, Word> neuroneCache = Maps.newHashMap();
    private Map<String, Sentence> sentenceCache = Maps.newHashMap();

    public Word neuron(String token) {
        if(!neuroneCache.containsKey(token)){
            neuroneCache.put(token, new Word(token));
        }
        Word word = neuroneCache.get(token);
        word.ink();
        return word;
    }

    public Sentence sentence(String sentenceString, List<Word> words) {
        String key = wordsKey(words);
        if(!sentenceCache.containsKey(key)){
            sentenceCache.put(key, new Sentence(sentenceString, words));
        }
        Sentence sentence = sentenceCache.get(key);
        return sentence;
    }

    private String wordsKey(List<Word> words) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Word word : words) {
            stringBuffer.append(word.getWord());
        }
        return stringBuffer.toString();
    }

    public Collection<Sentence> getSentences() {
        return sentenceCache.values();
    }
}
