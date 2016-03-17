package com.kemi.system;

import java.util.List;

/**
 * Created by Eugene on 17.03.2016.
 */
public class Sentence {

    private String sentence;
    private List<Word> words;

    public Sentence(String sentence, List<Word> words) {
        this.sentence = sentence;
        this.words = words;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sentence)) return false;

        Sentence sentence1 = (Sentence) o;

        if (sentence != null ? !sentence.equals(sentence1.sentence) : sentence1.sentence != null) return false;
        return words != null ? words.equals(sentence1.words) : sentence1.words == null;

    }

    @Override
    public int hashCode() {
        int result = sentence != null ? sentence.hashCode() : 0;
        result = 31 * result + (words != null ? words.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "sentence='" + sentence + '\'' +
                ", words=" + words +
                '}';
    }
}
