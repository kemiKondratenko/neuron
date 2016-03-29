package com.kemi.entities;

import javax.persistence.*;

/**
 * Created by Eugene on 29.03.2016.
 */
@Entity
@Table(name = "text_words")
public class TextWordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "word_id", referencedColumnName="id")
    private WordEntity wordEntity;

    @ManyToOne
    @JoinColumn(name = "link_id", referencedColumnName="id")
    private PdfLink pdfLink;


    @Column(name = "count")
    private int count;

    public TextWordEntity() {}

    public TextWordEntity(PdfLink pdfLink, WordEntity wordEntity) {
        this.wordEntity = wordEntity;
        this.pdfLink = pdfLink;
        this.count = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WordEntity getWordEntity() {
        return wordEntity;
    }

    public void setWordEntity(WordEntity wordEntity) {
        this.wordEntity = wordEntity;
    }

    public PdfLink getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(PdfLink pdfLink) {
        this.pdfLink = pdfLink;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextWordEntity)) return false;

        TextWordEntity that = (TextWordEntity) o;

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
        return "TextWordEntity{" +
                "id=" + id +
                ", wordEntity=" + wordEntity +
                ", pdfLink=" + pdfLink +
                ", count=" + count +
                '}';
    }

    public String stringId() {
        return stringId(pdfLink.getId(), wordEntity.getId());
    }

    public static String stringId(String pdfLink, String wordEntity) {
        return "pdfLink=" + pdfLink +
                ", wordEntity=" + wordEntity;
    }

    public static String stringId(int pdfLink, int wordEntity) {
        return pdfLink +
                "," + wordEntity;
    }

    public TextWordEntity inc() {
        count++;
        return this;
    }
}
