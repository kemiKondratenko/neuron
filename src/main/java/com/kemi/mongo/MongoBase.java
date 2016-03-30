package com.kemi.mongo;

import com.kemi.entities.PdfLink;
import com.kemi.entities.WordEntity;
import com.kemi.entities.mongo.TextWordMongoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Created by Eugene on 30.03.2016.
 */
@Service
public class MongoBase {

    @Autowired
    private MongoOperations mongoOperations;

    public void create(PdfLink link, WordEntity wordE) {
        TextWordMongoEntity textWordEntity = find(link, wordE);
        if(textWordEntity == null){
            textWordEntity = new TextWordMongoEntity(link, wordE);
        }
        textWordEntity.inc();
        mongoOperations.save(textWordEntity);
    }

    private TextWordMongoEntity find(PdfLink link, WordEntity wordE) {
        Query findQuery = new Query();
        Criteria mainCriteria = Criteria.where("pdfLink").is(link.getId()).
                andOperator(Criteria.where("wordEntity").is(wordE.getId()));
        findQuery.addCriteria(mainCriteria);
        return mongoOperations.findOne(findQuery, TextWordMongoEntity.class);
    }
}
