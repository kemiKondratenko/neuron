package com.kemi.database;

import com.google.common.collect.Maps;
import com.kemi.entities.WordEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<WordEntity> find(String href) {
        return entitiesDao.get(WordEntity.class, Restrictions.eq("word", href));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public WordEntity create(String href) {
        List<WordEntity> forUrl = find(href);
        forUrl.remove(null);
        Integer i;
        if(CollectionUtils.isEmpty(forUrl)) {
            WordEntity pdfLink = new WordEntity(href);
            i = entitiesDao.save(pdfLink);
            cache.put(href, entitiesDao.get(WordEntity.class, i));
        }
        return cache.get(href);
    }
}
