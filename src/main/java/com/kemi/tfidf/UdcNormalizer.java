package com.kemi.tfidf;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kemi.database.EntitiesDao;
import com.kemi.database.LinkToUdcDao;
import com.kemi.database.UdcDao;
import com.kemi.entities.LinkToUdc;
import com.kemi.entities.PdfLink;
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

    public String formNormalizedUdc(int chars) {
        Map<String, Set<UdcEntity>> normalizedUdcToIds = getNormalizedMap(chars);
        for (String normalizedUdc : normalizedUdcToIds.keySet()) {
            UdcEntity udcEntity = udcDao.create(normalizedUdc, chars);
            for (UdcEntity notNormalizedUdc : normalizedUdcToIds.get(normalizedUdc)) {
                for (LinkToUdc linkToUdc : notNormalizedUdc.getLinkToUdcs()) {
                    if (!contains(linkToUdc.getPdfLink().getLinkToUdcs(), udcEntity)) {
                        linkToUdcDao.create(linkToUdc.getPdfLink(), udcEntity);
                        createNormalizedWordEntyti(linkToUdc.getPdfLink(), udcEntity);

                    }
                }
            }
        }
        return "";
    }

    private boolean contains(Set<LinkToUdc> linkToUdcs, UdcEntity udcEntity) {
        for (LinkToUdc linkToUdc : linkToUdcs) {
            if(linkToUdc.getUdcEntity().equals(udcEntity)){
                return true;
            }
        }
        return false;
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
                createNormalizedWordEntyti(linkToUdc.getPdfLink(), udcEntity);
            }
            udcEntity.setIndexed(true);
            entitiesDao.save(udcEntity);
        }
        return "";
    }

    public void createNormalizedWordEntyti(PdfLink pdfLink, UdcEntity udcEntity) {
        for (TextWordMongoEntity textWordMongoEntity : mongoBase.getPdfLinkTerms(pdfLink.getId())) {
            mongoBase.create(udcEntity, textWordMongoEntity.getWordEntity());
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String countPossibility(int normalization) {
        Double n = Double.valueOf(entitiesDao.executeCount(
                " SELECT count(*) FROM neuron.pdf_link as pdf where " +
                        " exists (select * from link_to_udc as ln where " +
                        " ln.pdfLink_id = pdf.id " +
                        " AND exists " +
                        " (select * from udc as ud where ln.udcEntity_id = ud.id AND normalization = " + normalization + " ));").longValue());
        for (UdcEntity udcEntity : entitiesDao.get(
                UdcEntity.class,
                Restrictions.eq("normalization", Integer.valueOf(normalization)),
                Restrictions.eq("indexed", true)
        )
                ) {
            Double nC = Double.valueOf(udcEntity.getLinkToUdcs().size());
            udcEntity.setPossibilityOfUdc(nC / n);
            entitiesDao.save(udcEntity);
        }
        return "";
    }
}
