package com.kemi.mongo;

import com.kemi.entities.PdfLink;
import com.kemi.entities.UdcEntity;
import com.kemi.entities.mongo.NormalizedUdcMongoEnity;
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

    public void create(UdcEntity udcEntity, String wordEntity) {
        NormalizedUdcMongoEnity textWordEntity = find(udcEntity, wordEntity);
        if(textWordEntity == null){
            textWordEntity = new NormalizedUdcMongoEnity(udcEntity.getId(), wordEntity);
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

    private NormalizedUdcMongoEnity find(UdcEntity udcEntity, String wordEntity) {
        Query findQuery = new Query();
        Criteria mainCriteria = Criteria.where("normalizedUdc").is(udcEntity.getId()).
                andOperator(Criteria.where("wordEntity").is(wordEntity));
        findQuery.addCriteria(mainCriteria);
        return mongoOperations.findOne(findQuery, NormalizedUdcMongoEnity.class);
    }

    public int getPdfLinkTermsAmount(int id) {
        TypedAggregation<TextWordMongoEntity> agg = Aggregation.newAggregation(
                TextWordMongoEntity.class,
                Aggregation.match(Criteria.where("pdfLink").is(id)),
                Aggregation.group().sum("count").as("count")
        );
        AggregationResults<TextWordMongoEntity> result = mongoOperations.aggregate(agg, TextWordMongoEntity.class);
        List<TextWordMongoEntity> stateStatsList = result.getMappedResults();
        if(stateStatsList.isEmpty()){
            return 0;
        }
        return stateStatsList.get(0).getCount();
    }


    public List<TextWordMongoEntity> getPdfLinkTerms(int id) {
        Query findQuery = new Query();
        Criteria mainCriteria = Criteria.where("pdfLink").is(id);
        findQuery.addCriteria(mainCriteria);
        return mongoOperations.find(findQuery, TextWordMongoEntity.class);
    }

    public List<TextWordMongoEntity> getPdfLinkTerms(String id) {
        Query findQuery = new Query();
        Criteria mainCriteria = Criteria.where("wordEntity").is(id);
        findQuery.addCriteria(mainCriteria);
        return mongoOperations.find(findQuery, TextWordMongoEntity.class);
    }

    public <T> void save(T textWordMongoEntity) {
        mongoOperations.save(textWordMongoEntity);
    }

    public <T> List<T> get(Class<T> wordMongoEntityClass) {
        return mongoOperations.findAll(wordMongoEntityClass);
    }

    public int getWordsCount() {
        TypedAggregation<TextWordMongoEntity> agg = Aggregation.newAggregation(
                TextWordMongoEntity.class,
                Aggregation.group().sum("count").as("count")
        );
        AggregationResults<TextWordMongoEntity> result = mongoOperations.aggregate(agg, TextWordMongoEntity.class);
        List<TextWordMongoEntity> stateStatsList = result.getMappedResults();
        return stateStatsList.get(0).getCount();
    }

    public int getWordsCount(String id) {
        TypedAggregation<TextWordMongoEntity> agg = Aggregation.newAggregation(
                TextWordMongoEntity.class,
                Aggregation.match(Criteria.where("wordEntity").is(id)),
                Aggregation.group().sum("count").as("count")
        );
        AggregationResults<TextWordMongoEntity> result = mongoOperations.aggregate(agg, TextWordMongoEntity.class);
        List<TextWordMongoEntity> stateStatsList = result.getMappedResults();
        return stateStatsList.get(0).getCount();
    }

    public WordMongoEntity getWord(String wordEntity) {
        Query findQuery = new Query();
        Criteria mainCriteria = Criteria.where("id").is(wordEntity);
        findQuery.addCriteria(mainCriteria);
        return mongoOperations.findOne(findQuery, WordMongoEntity.class);
    }

    public List<TextWordMongoEntity> getPdfLinkTerms() {
        return mongoOperations.findAll(TextWordMongoEntity.class);
    }

    public int getUdcTermsAmount(int id) {
        TypedAggregation<NormalizedUdcMongoEnity> agg = Aggregation.newAggregation(
                NormalizedUdcMongoEnity.class,
                Aggregation.match(Criteria.where("normalizedUdc").is(id)),
                Aggregation.group().sum("count").as("count")
        );
        AggregationResults<NormalizedUdcMongoEnity> result = mongoOperations.aggregate(agg, NormalizedUdcMongoEnity.class);
        List<NormalizedUdcMongoEnity> stateStatsList = result.getMappedResults();
        if(stateStatsList.isEmpty()){
            return 0;
        }
        return stateStatsList.get(0).getCount();
    }

    public List<NormalizedUdcMongoEnity> getNormalizedUdcTerms(int id) {
        Query findQuery = new Query();
        Criteria mainCriteria = Criteria.where("normalizedUdc").is(id);
        findQuery.addCriteria(mainCriteria);
        return mongoOperations.find(findQuery, NormalizedUdcMongoEnity.class);
    }

    public List<NormalizedUdcMongoEnity> getNormalizedUdcTerms(String id) {
        Query findQuery = new Query();
        Criteria mainCriteria = Criteria.where("wordEntity").is(id);
        findQuery.addCriteria(mainCriteria);
        return mongoOperations.find(findQuery, NormalizedUdcMongoEnity.class);
    }

    public Integer getNormalizedUdcTermCount(String wordEntity) {
        TypedAggregation<NormalizedUdcMongoEnity> agg = Aggregation.newAggregation(
                NormalizedUdcMongoEnity.class,
                Aggregation.match(Criteria.where("wordEntity").is(wordEntity)),
                Aggregation.group().sum("count").as("count")
        );
        AggregationResults<NormalizedUdcMongoEnity> result = mongoOperations.aggregate(agg, NormalizedUdcMongoEnity.class);
        List<NormalizedUdcMongoEnity> stateStatsList = result.getMappedResults();
        if(stateStatsList.isEmpty()){
            return 0;
        }
        return stateStatsList.get(0).getCount();
    }

    public List<NormalizedUdcMongoEnity> getNormalizedAndUniqueUdcTerms(String id) {
        Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("wordEntity").is(id));
        findQuery.addCriteria(Criteria.where("unique").is(Boolean.TRUE));
        return mongoOperations.find(findQuery, NormalizedUdcMongoEnity.class);
    }

    public List<NormalizedUdcMongoEnity> getNormalizedAndUniqueUdcTerms() {
        Query findQuery = new Query();
        findQuery.addCriteria(Criteria.where("unique").is(Boolean.TRUE));
        return mongoOperations.find(findQuery, NormalizedUdcMongoEnity.class);
    }
}
