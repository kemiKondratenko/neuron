package com.kemi.mongo;

import com.kemi.database.EntitiesDao;
import com.kemi.entities.TextWordEntity;
import com.kemi.entities.mongo.TextWordMongoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Eugene on 30.03.2016.
 */
@Service
public class MongoBase {

    private String textWords = "text_words";

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private EntitiesDao entitiesDao;

    public void pushAllToMongo(){
        List<TextWordEntity> textWordEntities = entitiesDao.get(TextWordEntity.class);
        for (TextWordEntity textWordEntity : textWordEntities) {
            mongoOperations.save(new TextWordMongoEntity(textWordEntity), textWords);
        }
    }

    public void getAll(){
        List<TextWordMongoEntity> textWordEntities = mongoOperations.findAll(TextWordMongoEntity.class);
    }
}
