package com.kemi.entities.mongo;

import org.springframework.data.annotation.Id;

/**
 * Created by Eugene on 30.03.2016.
 */
public class WordMongoEntity {

    @Id
    private String id;

    private String word;

    public WordMongoEntity() {
    }

    public WordMongoEntity(String word) {
        this.word = word;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        if (!(o instanceof WordMongoEntity)) return false;

        WordMongoEntity that = (WordMongoEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return word != null ? word.equals(that.word) : that.word == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WordMongoEntity{" +
                "id='" + id + '\'' +
                ", word='" + word + '\'' +
                '}';
    }
}
