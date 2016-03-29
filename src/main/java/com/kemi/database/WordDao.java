package com.kemi.database;

import com.google.common.collect.Maps;
import com.kemi.entities.WordEntity;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Eugene on 27.03.2016.
 */
@Service
public class WordDao {

    @Autowired
    private EntitiesDao entitiesDao;

    private Map<String, WordEntity> cache = Maps.newHashMap();

    public WordEntity find(String href) {
        if(!cache.containsKey(href)){
            List<WordEntity> wordEntities = entitiesDao.get(WordEntity.class, Restrictions.eq("word", href));
            if(!wordEntities.isEmpty())
                cache.put(href, wordEntities.get(0));
        }
        return cache.get(href);
    }

    public WordEntity create(String href) {
        WordEntity forUrl = find(href);
        Integer i;
        if(forUrl == null) {
            WordEntity pdfLink = new WordEntity(href);
            i = entitiesDao.save(pdfLink);
            cache.put(href, entitiesDao.get(WordEntity.class, i));
            forUrl = cache.get(href);
        }
        return forUrl;
    }
}
