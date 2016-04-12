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

    @Column(name = "possibilityOfUdc")
    private Double possibilityOfUdc;

    @Column(name = "indexed")
    private Boolean indexed;

    @OneToMany(mappedBy = "udcEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LinkToUdc> linkToUdcs;

    public UdcEntity() {
    }

    public UdcEntity(String udc) {
        this.udc = udc;
        this.indexed = false;
        this.possibilityOfUdc = 0.;
    }

    public UdcEntity(String udc, Integer normalization) {
        this.udc = udc;
        this.normalization = normalization;
        this.indexed = false;
        this.possibilityOfUdc = 0.;
    }

    public UdcEntity(UdcEntity udcEntity) {
        this.id = udcEntity.id;
        this.udc = udcEntity.udc;
        this.linkToUdcs = Sets.newHashSet();
        for (LinkToUdc linkToUdc : udcEntity.getLinkToUdcs()) {
            this.linkToUdcs.add(new LinkToUdc(linkToUdc));
        }
        this.normalization = udcEntity.getNormalization();
        this.possibilityOfUdc = 0.;
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

    public Double getPossibilityOfUdc() {
        return possibilityOfUdc;
    }

    public void setPossibilityOfUdc(Double possibilityOfUdc) {
        this.possibilityOfUdc = possibilityOfUdc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UdcEntity)) return false;

        UdcEntity udcEntity = (UdcEntity) o;

        if (id != udcEntity.id) return false;
        if (!udc.equals(udcEntity.udc)) return false;
        return normalization == udcEntity.normalization || (normalization != null && normalization.equals(udcEntity.normalization));

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + udc.hashCode();
        result = normalization == null ? 1 : 31 * result + normalization.hashCode();
        return result;
    }
}
