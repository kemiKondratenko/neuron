package com.kemi.entities;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Eugene on 27.03.2016.
 */
@Entity
@Table(name = "udc")
public class UdcEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "udc")
    private String udc;

    @Column(name = "normalization")
    private Integer normalization;

    @Column(name = "indexed")
    private Boolean indexed;

    @OneToMany(mappedBy = "udcEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LinkToUdc> linkToUdcs;

    public UdcEntity() {
    }

    public UdcEntity(String udc) {
        this.udc = udc;
        this.indexed = false;
    }

    public UdcEntity(String udc, Integer normalization) {
        this.udc = udc;
        this.normalization = normalization;
        this.indexed = false;
    }

    public UdcEntity(UdcEntity udcEntity) {
        this.id = udcEntity.id;
        this.udc = udcEntity.udc;
        this.linkToUdcs = Sets.newHashSet();
        for (LinkToUdc linkToUdc : udcEntity.getLinkToUdcs()) {
            this.linkToUdcs.add(new LinkToUdc(linkToUdc));
        }
        this.normalization = udcEntity.getNormalization();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUdc() {
        return udc;
    }

    public void setUdc(String udc) {
        this.udc = udc;
    }

    public Set<LinkToUdc> getLinkToUdcs() {
        return linkToUdcs;
    }

    public void setLinkToUdcs(Set<LinkToUdc> linkToUdcs) {
        this.linkToUdcs = linkToUdcs;
    }

    public Integer getNormalization() {
        return normalization;
    }

    public void setNormalization(Integer normalization) {
        this.normalization = normalization;
    }

    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }
}
