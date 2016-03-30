package com.kemi.entities.mongo;

import com.kemi.entities.TextWordEntity;
import org.springframework.data.annotation.Id;

/**
 * Created by Eugene on 30.03.2016.
 */
public class TextWordMongoEntity {

    @Id
    private int id;

    private Integer wordEntity;

    private Integer pdfLink;

    private int count;

    public TextWordMongoEntity() {
    }

    public TextWordMongoEntity(int id, Integer wordEntity, Integer pdfLink, int count) {
        this.id = id;
        this.wordEntity = wordEntity;
        this.pdfLink = pdfLink;
        this.count = count;
    }

    public TextWordMongoEntity(TextWordEntity textWordEntity) {
        this.wordEntity = textWordEntity.getWordEntity().getId();
        this.pdfLink = textWordEntity.getPdfLink().getId();
        this.count = textWordEntity.getCount();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getWordEntity() {
        return wordEntity;
    }

    public void setWordEntity(Integer wordEntity) {
        this.wordEntity = wordEntity;
    }

    public Integer getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(Integer pdfLink) {
        this.pdfLink = pdfLink;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String stringId() {
        return stringId(pdfLink, wordEntity);
    }

    public static String stringId(String pdfLink, String wordEntity) {
        return "pdfLink=" + pdfLink +
                ", wordEntity=" + wordEntity;
    }

    public static String stringId(int pdfLink, int wordEntity) {
        return pdfLink +
                "," + wordEntity;
    }

    public TextWordMongoEntity inc() {
        count++;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextWordMongoEntity)) return false;

        TextWordMongoEntity that = (TextWordMongoEntity) o;

        if (id != that.id) return false;
        if (count != that.count) return false;
        if (wordEntity != null ? !wordEntity.equals(that.wordEntity) : that.wordEntity != null) return false;
        return pdfLink != null ? pdfLink.equals(that.pdfLink) : that.pdfLink == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (wordEntity != null ? wordEntity.hashCode() : 0);
        result = 31 * result + (pdfLink != null ? pdfLink.hashCode() : 0);
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return "TextWordMongoEntity{" +
                "id=" + id +
                ", wordEntity=" + wordEntity +
                ", pdfLink=" + pdfLink +
                ", count=" + count +
                '}';
    }
}
