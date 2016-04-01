package com.kemi.service;

import com.google.common.collect.Lists;
import com.kemi.database.EntitiesDao;
import com.kemi.entities.JsonDots;
import com.kemi.entities.PdfLink;
import com.kemi.entities.UdcEntity;
import com.kemi.storage.crawler.WebCrawler;
import com.kemi.tfidf.DocumentParser;
import com.kemi.tfidf.TfIdf;
import com.kemi.tfidf.UdcNormalizer;
import com.kemi.udc.UdcFinder;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URL;
import java.util.Collection;
import java.util.List;

/**
 * Created by Eugene on 16.03.2016.
 */
@Service
public class BuilderService {

    public static final Integer NORMALIZATION = 1;

    @Autowired
    private EntitiesDao entitiesDao;

    @Autowired
    private WebCrawler webCrawler;
    @Autowired
    private UdcFinder udcFinder;
    @Autowired
    private UdcNormalizer udcNormalizer;

    @Autowired
    private DocumentParser documentParser;
    @Autowired
    private TfIdf tfIdf;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void get() {
        try {
            webCrawler.start(new URL("http://nz.ukma.edu.ua/"), 6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Collection<UdcEntity> find() {
        List<UdcEntity> udcEntities = Lists.newArrayList();
        for (UdcEntity udcEntity : entitiesDao.get(UdcEntity.class, Restrictions.eq("normalization", 1))) {
            udcEntities.add(new UdcEntity(udcEntity));
        }
        return udcEntities;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Number count() {
        return entitiesDao.count(PdfLink.class);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String findUdc() {
        return udcFinder.index();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Number udcCount() {
        return entitiesDao.count(UdcEntity.class);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String index() {
        documentParser.parseFiles();
        return "OK";
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String ctf() {
        return tfIdf.ctfUdc(NORMALIZATION);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String cidf() {
        return tfIdf.cidf();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<JsonDots> getDots() {
        return tfIdf.getDots();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String formNormalizedUdc() {
        return udcNormalizer.formNormalizedUdc(NORMALIZATION);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String linkWordsToNormalizedUdc() {
        return udcNormalizer.linkWordsToNormalizedUdc(NORMALIZATION);
    }
}
