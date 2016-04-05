package com.kemi.clastering;

import com.google.common.collect.Maps;
import com.kemi.entities.mongo.NormalizedUdcMongoEnity;
import com.kemi.entities.mongo.WordMongoEntity;
import com.kemi.mongo.MongoBase;
import com.kemi.service.text.load.Loader;
import com.kemi.tfidf.ParseAndBuilder;
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

    public Map<Integer, Double> cluster(){
        List<WordMongoEntity> wordMongoEntities = parseAndBuilder.parseAndBuildWords(
                loader.loadText("http://rht-journal.kpi.ua/article/download/35774/32011"));
        Map<Integer, Double> result = Maps.newHashMap();
        for (WordMongoEntity wordMongoEntity : wordMongoEntities) {
            for (NormalizedUdcMongoEnity normalizedUdcMongoEnity : mongoBase.getNormalizedAndUniqueUdcTerms(wordMongoEntity.getId())) {
                if(!result.containsKey(normalizedUdcMongoEnity.getNormalizedUdc())){
                    result.put(normalizedUdcMongoEnity.getNormalizedUdc(), Double.valueOf(0));
                }
                Double aDouble = result.get(normalizedUdcMongoEnity.getNormalizedUdc()) + normalizedUdcMongoEnity.getCommon();
                result.put(normalizedUdcMongoEnity.getNormalizedUdc(), aDouble);}
        }
        return result;
    }
}
