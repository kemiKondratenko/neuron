package com.kemi.entities;

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

    @OneToMany(mappedBy = "udcEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LinkToUdc> linkToUdcs;

    public UdcEntity() {
    }

    public UdcEntity(String udc) {
        this.udc = udc;
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
}
