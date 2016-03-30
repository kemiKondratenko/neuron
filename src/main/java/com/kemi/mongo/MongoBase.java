package com.kemi.mongo;

import com.kemi.entities.PdfLink;
import com.kemi.entities.mongo.TextWordMongoEntity;
import com.kemi.entities.mongo.WordMongoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Eugene on 30.03.2016.
 */
@Service
public class MongoBase {

    @Autowired
    private MongoOperations mongoOperations;

    public void create(PdfLink link, WordMongoEntity wordE) {
        TextWordMongoEntity textWordEntity = find(link, wordE);
        if(textWordEntity == null){
            textWordEntity = new TextWordMongoEntity(link, wordE);
        }
        textWordEntity.inc();
        mongoOperations.save(textWordEntity);
    }

    private TextWordMongoEntity find(PdfLink link, WordMongoEntity wordE) {
        Query findQuery = new Query();
        Criteria mainCriteria = Criteria.where("pdfLink").is(link.getId()).
                andOperator(Criteria.where("wordEntity").is(wordE.getId()));
        findQuery.addCriteria(mainCriteria);
        return mongoOperations.findOne(findQuery, TextWordMongoEntity.class);
    }

    public int getPdfLinkTermsAmount(int id) {
        TypedAggregation<TextWordMongoEntity> agg = Aggregation.newAggregation(
                TextWordMongoEntity.class,
                Aggregation.group("pdfLink").sum("count").as("count"),
                Aggregation.match(Criteria.where("id").is(id))
        );
        AggregationResults<TextWordMongoEntity> result = mongoOperations.aggregate(agg, TextWordMongoEntity.class);
        List<TextWordMongoEntity> stateStatsList = result.getMappedResults();
        return stateStatsList.get(0).getCount();
    }


}
