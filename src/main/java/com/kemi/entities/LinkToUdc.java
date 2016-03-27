package com.kemi.entities;

import javax.persistence.*;

/**
 * Created by Eugene on 27.03.2016.
 */
@Entity
@Table(name = "link_to_udc")
public class LinkToUdc {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "pdfLink_id", referencedColumnName="id")
    private PdfLink pdfLink;

    @ManyToOne
    @JoinColumn(name = "udcEntity_id", referencedColumnName="id")
    private UdcEntity udcEntity;

    public LinkToUdc() {}

    public LinkToUdc(PdfLink pdfLink, UdcEntity udcEntity) {
        this.pdfLink = pdfLink;
        this.udcEntity = udcEntity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PdfLink getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(PdfLink pdfLink) {
        this.pdfLink = pdfLink;
    }

    public UdcEntity getUdcEntity() {
        return udcEntity;
    }

    public void setUdcEntity(UdcEntity udcEntity) {
        this.udcEntity = udcEntity;
    }
}
