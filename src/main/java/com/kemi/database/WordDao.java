package com.kemi.database;

import com.kemi.entities.mongo.WordMongoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Created by Eugene on 27.03.2016.
 */
@Service
public class WordDao {

    @Autowired
    private MongoOperations mongoOperations;

    private static  int two = 0;

    public WordMongoEntity find(String href) {
        Query findQuery = new Query();
        Criteria mainCriteria = Criteria.where("word").is(href);
        findQuery.addCriteria(mainCriteria);
        return mongoOperations.findOne(findQuery, WordMongoEntity.class);
    }

    @Cacheable("wordCache")
    public WordMongoEntity create(String href) {
        WordMongoEntity forUrl = find(href);
        two++;
        if(forUrl == null) {
            WordMongoEntity pdfLink = new WordMongoEntity(href);
            mongoOperations.save(pdfLink);
            forUrl = pdfLink;
        }
        return forUrl;
    }
}
