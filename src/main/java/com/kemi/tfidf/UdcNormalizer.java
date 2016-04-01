package com.kemi.tfidf;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kemi.database.EntitiesDao;
import com.kemi.database.LinkToUdcDao;
import com.kemi.database.UdcDao;
import com.kemi.database.WordDao;
import com.kemi.entities.LinkToUdc;
import com.kemi.entities.UdcEntity;
import com.kemi.entities.mongo.TextWordMongoEntity;
import com.kemi.mongo.MongoBase;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Set;

/**
 * Created by Eugene on 01.04.2016.
 */
@Service
public class UdcNormalizer {

    @Autowired
    private EntitiesDao entitiesDao;
    @Autowired
    private UdcDao udcDao;
    @Autowired
    private LinkToUdcDao linkToUdcDao;


    @Autowired
    private MongoBase mongoBase;
    @Autowired
    private WordDao wordDao;

    public String formNormalizedUdc(int chars) {
        Map<String, Set<UdcEntity>> normalizedUdcToIds = getNormalizedMap(chars);
        for (String normalizedUdc : normalizedUdcToIds.keySet()) {
            UdcEntity udcEntity = udcDao.create(normalizedUdc, chars);
            for (UdcEntity notNormalizedUdc : normalizedUdcToIds.get(normalizedUdc)) {
                for (LinkToUdc linkToUdc : notNormalizedUdc.getLinkToUdcs()) {
                    linkToUdcDao.create(linkToUdc.getPdfLink(), udcEntity);
                }
            }
        }
        return "";
    }

    private Map<String, Set<UdcEntity>> getNormalizedMap(int chars) {
        Map<String, Set<UdcEntity>> normalizedUdcToIds = Maps.newHashMap();
        for (UdcEntity udcEntity : udcDao.getAll()) {
            String normalizedUdc = udcEntity.getUdc().substring(0, chars);
            if (!normalizedUdcToIds.containsKey(normalizedUdc)) {
                normalizedUdcToIds.put(normalizedUdc, Sets.<UdcEntity>newHashSet());
            }
            normalizedUdcToIds.get(normalizedUdc).add(udcEntity);
        }
        return normalizedUdcToIds;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String linkWordsToNormalizedUdc(int normalization) {
        for (UdcEntity udcEntity : entitiesDao.get(
                UdcEntity.class,
                Restrictions.eq("normalization", Integer.valueOf(normalization)),
                Restrictions.isNull("indexed")
        )
                ) {
            for (LinkToUdc linkToUdc : udcEntity.getLinkToUdcs()) {
                for (TextWordMongoEntity textWordMongoEntity : mongoBase.getPdfLinkTerms(linkToUdc.getPdfLink().getId())) {
                    mongoBase.create(udcEntity, textWordMongoEntity.getWordEntity());
                }
            }
            udcEntity.setIndexed(true);
            entitiesDao.save(udcEntity);
        }
        return "";
    }
}
