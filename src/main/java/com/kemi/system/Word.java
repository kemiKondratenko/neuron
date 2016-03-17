package com.kemi.system;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Eugene on 16.03.2016.
 */
public class Word {

    private List<Connection> words;
    private String word;
    private int count;

    public Word(String word) {
        this.word = word;
        words = Lists.newArrayList();
        count = 0;
    }

    public List<Connection> getWords() {
        return words;
    }

    public void setWords(List<Connection> words) {
        this.words = words;
    }

    public void addWords(List<Connection> neurons) {
        this.words.addAll(neurons);
    }

    public void addWord(Connection neuron) {
        this.words.add(neuron);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;

        Word word = (Word) o;

        if (words != null ? !words.equals(word.words) : word.words != null) return false;
        return this.word != null ? this.word.equals(word.word) : word.word == null;

    }

    @Override
    public int hashCode() {
        int result = words != null ? words.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Neuron{" +
                "words=" + words +
                ", word='" + word + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    public void ink() {
        count++;
    }
}
