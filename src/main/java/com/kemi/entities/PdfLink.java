package com.kemi.entities;

import javax.persistence.*;

/**
 * Created by Eugene on 27.03.2016.
 */

@Entity
@Table(name = "pdf_link")
public class PdfLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "pdf_link")
    private String pdfLink;

    public PdfLink() {}

    public PdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    @Override
    public String toString() {
        return "PdfLink{" +
                "id=" + id +
                ", pdfLink='" + pdfLink + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdfLink)) return false;

        PdfLink pdfLink1 = (PdfLink) o;

        if (id != pdfLink1.id) return false;
        return pdfLink != null ? pdfLink.equals(pdfLink1.pdfLink) : pdfLink1.pdfLink == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (pdfLink != null ? pdfLink.hashCode() : 0);
        return result;
    }
}
