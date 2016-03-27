package com.kemi.database;

import com.google.common.collect.Maps;
import com.kemi.system.Word;
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

    private Map<String, Word> cache = Maps.newHashMap();

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<Word> find(String href) {
        return entitiesDao.get(Word.class, Restrictions.eq("word", href));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Word create(String href) {
        List<Word> forUrl = find(href);
        forUrl.remove(null);
        Integer i;
        if(CollectionUtils.isEmpty(forUrl)) {
            Word pdfLink = new Word(href);
            i = entitiesDao.save(pdfLink);
            cache.put(href, entitiesDao.get(Word.class, i));
        }
        return cache.get(href);
    }
}
