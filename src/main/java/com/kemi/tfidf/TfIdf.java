package com.kemi.tfidf;

import com.google.common.collect.Lists;
import com.kemi.database.EntitiesDao;
import com.kemi.entities.JsonDots;
import com.kemi.entities.PdfLink;
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
            double tfBuffer = 0;
            double count = 0;
            for (TextWordMongoEntity textWordMongoEntity : mongoBase.getPdfLinkTerms(wordMongoEntity.getId())) {
                tfBuffer += textWordMongoEntity.getTf();
                count++;
            }
            dots.add(new JsonDots(wordMongoEntity.getIdf(), tfBuffer/count, wordMongoEntity.getWord()));
        }
        return dots;
    }
}
