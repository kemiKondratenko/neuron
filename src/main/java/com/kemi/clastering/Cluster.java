package com.kemi.clastering;

import com.google.common.collect.Maps;
import com.kemi.database.LinksDao;
import com.kemi.database.UdcDao;
import com.kemi.entities.PdfLink;
import com.kemi.entities.mongo.NormalizedUdcMongoEnity;
import com.kemi.entities.mongo.TextWordMongoEntity;
import com.kemi.entities.mongo.WordMongoEntity;
import com.kemi.mongo.MongoBase;
import com.kemi.service.text.load.Loader;
import com.kemi.tfidf.ParseAndBuilder;
import com.kemi.tfidf.TfIdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Eugene on 05.04.2016.
 */
@Service
public class Cluster {

    @Autowired
    private ParseAndBuilder parseAndBuilder;
    @Autowired
    private Loader loader;
    @Autowired
    private MongoBase mongoBase;
    @Autowired
    private TfIdf tfIdf;
    @Autowired
    private LinksDao linksDao;
    @Autowired
    private UdcDao udcDao;

    public Map<String, Double> cluster() {
        //PdfLink pdfLink =  linksDao.create("http://rht-journal.kpi.ua/article/download/35773/32010");
       PdfLink pdfLink = linksDao.get(576);
        List<WordMongoEntity> wordMongoEntities = parseAndBuilder.parseAndBuildAndReturnWords(pdfLink,
                loader.loadText(pdfLink.getPdfLink()));
        for (WordMongoEntity wordMongoEntity : wordMongoEntities) {
            mongoBase.create(pdfLink, wordMongoEntity);
        }
        tfIdf.ctf(pdfLink.getId());


        Double magnitude1 = 0.0;
        Map<Integer, Double> dotProduct = Maps.newHashMap();
        Map<Integer, Double> magnitude2 = Maps.newHashMap();
        for (TextWordMongoEntity textWordMongoEntity : mongoBase.getPdfLinkTerms(pdfLink.getId())) {
            WordMongoEntity wordMongoEntity = mongoBase.getWord(textWordMongoEntity.getWordEntity());
            if (wordMongoEntity.getIdf() != null) {
                Double docVector1 = wordMongoEntity.getIdf() * textWordMongoEntity.getTf();
                magnitude1 += Math.pow(docVector1, 2);
                for (NormalizedUdcMongoEnity normalizedUdcMongoEnity : mongoBase.getNormalizedAndUniqueUdcTerms(wordMongoEntity.getId())) {
                    Double docVector2 = normalizedUdcMongoEnity.getCommon();
                    if (!dotProduct.containsKey(normalizedUdcMongoEnity.getNormalizedUdc())) {
                        magnitude2.put(normalizedUdcMongoEnity.getNormalizedUdc(), Double.valueOf(0));
                        dotProduct.put(normalizedUdcMongoEnity.getNormalizedUdc(), Double.valueOf(0));
                    }
                    Double aDouble = magnitude2.get(normalizedUdcMongoEnity.getNormalizedUdc()) + Math.pow(docVector2, 2);
                    magnitude2.put(normalizedUdcMongoEnity.getNormalizedUdc(), aDouble);
                    Double aDouble2 = dotProduct.get(normalizedUdcMongoEnity.getNormalizedUdc()) + docVector1 * docVector2;
                    dotProduct.put(normalizedUdcMongoEnity.getNormalizedUdc(), aDouble2);
                }
            }
        }
        Map<String, Double> result = Maps.newHashMap();
        for (Integer integer : dotProduct.keySet()) {
            if (magnitude1 != 0.0 || magnitude2.get(integer) != 0.0) {
                result.put(udcDao.get(integer).getUdc(), dotProduct.get(integer) / (magnitude1 * magnitude2.get(integer)));
            }
        }
        return result;
    }
}
