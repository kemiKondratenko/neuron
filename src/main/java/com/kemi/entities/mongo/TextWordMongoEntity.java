package com.kemi.entities.mongo;

import com.kemi.entities.PdfLink;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Eugene on 30.03.2016.
 */
public class TextWordMongoEntity {

    @Id
    private String id;

    private String wordEntity;

    private Integer pdfLink;

    @Field("count")
    private Integer count;

    private Double tf;

    public TextWordMongoEntity() {
    }

    public TextWordMongoEntity(int String, Integer wordEntity, Integer pdfLink, Integer count) {
        this.id = id;
        this.wordEntity = wordEntity.toString();
        this.pdfLink = pdfLink;
        this.count = count;
    }

    public TextWordMongoEntity(PdfLink link, WordMongoEntity wordE) {
        this.wordEntity = wordE.getId();
        this.pdfLink = link.getId();
        this.count = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWordEntity() {
        return wordEntity;
    }

    public void setWordEntity(String wordEntity) {
        this.wordEntity = wordEntity;
    }

    public Integer getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(Integer pdfLink) {
        this.pdfLink = pdfLink;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String stringId() {
        return stringId(pdfLink, wordEntity);
    }

    public Double getTf() {
        return tf;
    }

    public void setTf(Double tf) {
        this.tf = tf;
    }

    public static String stringId(Integer pdfLink, String wordEntity) {
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
        int result = id.hashCode();
        result = 31 * result + (wordEntity != null ? wordEntity.hashCode() : 0);
        result = 31 * result + (pdfLink != null ? pdfLink.hashCode() : 0);
        result = 31 * result + count.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TextWordMongoEntity{" +
                "id='" + id + '\'' +
                ", wordEntity=" + wordEntity +
                ", pdfLink=" + pdfLink +
                ", count='" + count + '\'' +
                '}';
    }
}
