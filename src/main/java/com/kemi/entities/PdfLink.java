package com.kemi.entities;

import javax.persistence.*;
import java.util.Set;

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

    @OneToMany(mappedBy = "pdfLink", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LinkToUdc> linkToUdcs;

    @Column(name = "indexed")
    private Boolean indexed;

    public PdfLink() {}

    public PdfLink(int id, String pdfLink) {
        this.id = id;
        this.pdfLink = pdfLink;
        this.indexed = false;
    }

    public PdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public PdfLink(PdfLink pdfLink) {
        this.id = pdfLink.id;
        this.pdfLink = pdfLink.pdfLink;
        this.indexed = pdfLink.indexed;
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

    public Set<LinkToUdc> getLinkToUdcs() {
        return linkToUdcs;
    }

    public void setLinkToUdcs(Set<LinkToUdc> linkToUdcs) {
        this.linkToUdcs = linkToUdcs;
    }

    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
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
