package com.kemi.entities;

import javax.persistence.*;

/**
 * Created by Eugene on 16.03.2016.
 */

@Entity
@Table(name = "words")
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "word")
    private String word;

    public WordEntity(String word) {
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (!(o instanceof WordEntity)) return false;

        WordEntity that = (WordEntity) o;

        if (id != that.id) return false;
        return word != null ? word.equals(that.word) : that.word == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WordEntity{" +
                "id=" + id +
                ", word='" + word + '\'' +
                '}';
    }
}
