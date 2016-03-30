package com.kemi.database;

import com.kemi.entities.WordEntity;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Eugene on 27.03.2016.
 */
@Service
public class WordDao {

    @Autowired
    private EntitiesDao entitiesDao;

    private static  int two = 0;

    public WordEntity find(String href) {
        List<WordEntity> wordEntities = entitiesDao.get(WordEntity.class, Restrictions.eq("word", href));
        if(!wordEntities.isEmpty())
            return wordEntities.get(0);
        return null;
    }

    @Cacheable("wordCache")
    public WordEntity create(String href) {
        WordEntity forUrl = find(href);
        two++;
        Integer i;
        if(forUrl == null) {
            WordEntity pdfLink = new WordEntity(href);
            i = entitiesDao.save(pdfLink);
            forUrl = entitiesDao.get(WordEntity.class, i);
        }
        return forUrl;
    }
}
