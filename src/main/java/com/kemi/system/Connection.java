package com.kemi.system;

import com.kemi.entities.WordEntity;

/**
 * Created by Eugene on 16.03.2016.
 */
public class Connection {

    private WordEntity word;
    private Integer weight;

    public Connection(WordEntity word, Integer weight) {
        this.word = word;
        this.weight = weight;
    }

    public WordEntity getWord() {
        return word;
    }

    public void setWord(WordEntity word) {
        this.word = word;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "neuron=" + word +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connection)) return false;

        Connection that = (Connection) o;

        if (word != null ? !word.equals(that.word) : that.word != null) return false;
        return weight != null ? weight.equals(that.weight) : that.weight == null;

    }

    @Override
    public int hashCode() {
        int result = word != null ? word.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        return result;
    }
}
