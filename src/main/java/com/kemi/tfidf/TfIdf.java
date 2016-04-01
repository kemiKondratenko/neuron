package com.kemi.tfidf;

import com.google.common.collect.Lists;
import com.kemi.database.EntitiesDao;
import com.kemi.entities.JsonDots;
import com.kemi.entities.PdfLink;
import com.kemi.entities.UdcEntity;
import com.kemi.entities.mongo.NormalizedUdcMongoEnity;
import com.kemi.entities.mongo.TextWordMongoEntity;
import com.kemi.entities.mongo.WordMongoEntity;
import com.kemi.mongo.MongoBase;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Eugene on 23.03.2016.
 */
@Service
public class TfIdf {

    @Autowired
    private EntitiesDao entitiesDao;

    @Autowired
    private MongoBase mongoBase;

    public String ctf() {
        for (PdfLink pdfLink : entitiesDao.get(PdfLink.class, Restrictions.eq("indexed", true))) {
            ctf(pdfLink.getId());
        }
        return "";
    }

    public String cidf() {
        Double wordsCount = Double.valueOf(mongoBase.getWordsCount());
        for (WordMongoEntity wordMongoEntity : mongoBase.get(WordMongoEntity.class)) {
            Double wordCount = Double.valueOf(mongoBase.getWordsCount(wordMongoEntity.getId()));
            wordMongoEntity.setIdf(1 + Math.log(wordsCount / wordCount));
            mongoBase.save(wordMongoEntity);
        }
        return "";
    }

    private void ctf(int id) {
        int docTerms = mongoBase.getPdfLinkTermsAmount(id);
        if(docTerms > 0) {
            for (TextWordMongoEntity textWordMongoEntity : mongoBase.getPdfLinkTerms(id)) {
                textWordMongoEntity.setTf(Double.valueOf(textWordMongoEntity.getCount()) / Double.valueOf(docTerms));
                mongoBase.save(textWordMongoEntity);
            }
        }
    }

    public List<JsonDots> getDots() {
        List<JsonDots> dots = Lists.newArrayList();
        for (WordMongoEntity wordMongoEntity : mongoBase.get(WordMongoEntity.class)) {
            for (NormalizedUdcMongoEnity textWordMongoEntity : mongoBase.getNormalizedUdcTerms(wordMongoEntity.getId())) {
                dots.add(new JsonDots(wordMongoEntity.getIdf(), textWordMongoEntity.getTf(), wordMongoEntity.getWord()));
            }
        }
        return dots;
    }

    public String ctfUdc(Integer normalization) {
        for (UdcEntity pdfLink : entitiesDao.get(UdcEntity.class, Restrictions.eq("normalization", normalization)
        , Restrictions.eq("indexed", Boolean.TRUE))) {
            ctfUdcCounter(pdfLink.getId());
        }
        return "";
    }

    private void ctfUdcCounter(int id) {
        int docTerms = mongoBase.getUdcTermsAmount(id);
        if(docTerms > 0) {
            for (NormalizedUdcMongoEnity normalizedUdcMongoEnity : mongoBase.getNormalizedUdcTerms(id)) {
                normalizedUdcMongoEnity.setTf(Double.valueOf(normalizedUdcMongoEnity.getCount()) / Double.valueOf(docTerms));
                mongoBase.save(normalizedUdcMongoEnity);
            }
        }
    }
}
