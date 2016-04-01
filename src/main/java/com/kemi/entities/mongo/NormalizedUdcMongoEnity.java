package com.kemi.entities.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Eugene on 01.04.2016.
 */
public class NormalizedUdcMongoEnity {

    @Id
    private String id;

    private String wordEntity;

    private Integer normalizedUdc;

    @Field("count")
    private Integer count;

    private Double tf;

    public NormalizedUdcMongoEnity() {
    }

    public NormalizedUdcMongoEnity(int normalizedUdc, String wordEntity) {
        this.normalizedUdc = normalizedUdc;
        this.wordEntity = wordEntity;
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

    public Integer getNormalizedUdc() {
        return normalizedUdc;
    }

    public void setNormalizedUdc(Integer normalizedUdc) {
        this.normalizedUdc = normalizedUdc;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getTf() {
        return tf;
    }

    public void setTf(Double tf) {
        this.tf = tf;
    }

    public NormalizedUdcMongoEnity inc() {
        count++;
        return this;
    }
}
