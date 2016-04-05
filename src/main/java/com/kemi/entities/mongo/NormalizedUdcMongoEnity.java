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

    private Double common;

    private Integer randomId;

    private Boolean unique;

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

    public Double getCommon() {
        return common;
    }

    public void setCommon(Double common) {
        this.common = common;
    }

    public Integer getRandomId() {
        return randomId;
    }

    public void setRandomId(Integer randomId) {
        this.randomId = randomId;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NormalizedUdcMongoEnity)) return false;

        NormalizedUdcMongoEnity that = (NormalizedUdcMongoEnity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (wordEntity != null ? !wordEntity.equals(that.wordEntity) : that.wordEntity != null) return false;
        if (normalizedUdc != null ? !normalizedUdc.equals(that.normalizedUdc) : that.normalizedUdc != null)
            return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (tf != null ? !tf.equals(that.tf) : that.tf != null) return false;
        if (common != null ? !common.equals(that.common) : that.common != null) return false;
        if (randomId != null ? !randomId.equals(that.randomId) : that.randomId != null) return false;
        return unique != null ? unique.equals(that.unique) : that.unique == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (wordEntity != null ? wordEntity.hashCode() : 0);
        result = 31 * result + (normalizedUdc != null ? normalizedUdc.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (tf != null ? tf.hashCode() : 0);
        result = 31 * result + (common != null ? common.hashCode() : 0);
        result = 31 * result + (randomId != null ? randomId.hashCode() : 0);
        result = 31 * result + (unique != null ? unique.hashCode() : 0);
        return result;
    }
}
