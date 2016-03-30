package com.kemi.database;

import com.google.common.collect.Maps;
import com.kemi.entities.PdfLink;
import com.kemi.entities.TextWordEntity;
import com.kemi.entities.WordEntity;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Eugene on 29.03.2016.
 */
@Service
public class TextWordEntityDao {

    @Autowired
    private EntitiesDao entitiesDao;

    private Map<String, TextWordEntity> cache = Maps.newHashMap();

    public TextWordEntity find(PdfLink link, WordEntity word) {
        if(cache.size() > 2000){
            cache.clear();
        }

        String key = TextWordEntity.stringId(link.getId(), word.getId());
        if(!cache.containsKey(key)){
            List<TextWordEntity> textWordEntities = entitiesDao.get(TextWordEntity.class,
                    Restrictions.eq("pdfLink.id", link.getId()),
                    Restrictions.eq("wordEntity.id", word.getId()));
            if(!textWordEntities.isEmpty())
                cache.put(key, textWordEntities.get(0));
        }
        return cache.get(key);
    }

    public TextWordEntity create(PdfLink link, WordEntity word) {
        TextWordEntity forUrl = find(link, word);
        if(forUrl == null) {
            TextWordEntity textWordEntity = new TextWordEntity(link, word);
            Integer i = entitiesDao.save(textWordEntity);
            forUrl = entitiesDao.get(TextWordEntity.class, i);
            cache.put(textWordEntity.stringId(), forUrl);
        } else {
            entitiesDao.update(forUrl.inc());
        }
        return forUrl;
    }
}
