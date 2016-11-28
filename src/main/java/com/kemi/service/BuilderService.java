package com.kemi.service;

import com.google.common.collect.Lists;
import com.kemi.clastering.Cluster;
import com.kemi.database.EntitiesDao;
import com.kemi.entities.JsonDots;
import com.kemi.entities.PdfLink;
import com.kemi.entities.UdcEntity;
import com.kemi.service.lucene.IndexService;
import com.kemi.storage.crawler.WebCrawler;
import com.kemi.tfidf.DocumentParser;
import com.kemi.tfidf.TfIdf;
import com.kemi.tfidf.UdcNormalizer;
import com.kemi.udc.UdcFinder;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private Cluster cluster;
    @Autowired
    private IndexService indexService;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void get() {
        try {
            webCrawler.start(new URL("http://kpi.ua/publication/"), 6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Collection<UdcEntity> find() {
        List<UdcEntity> udcEntities = Lists.newArrayList();
        /*for (UdcEntity udcEntity : entitiesDao.get(UdcEntity.class, Restrictions.eq("normalization", 1))) {
            udcEntities.add(new UdcEntity(udcEntity));
        }*/
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
        return tfIdf.ctf();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String ctfUdc() {
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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Map<String, Double> cluster() {
        return cluster.cluster();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String countPossibility() {
        return udcNormalizer.countPossibility(NORMALIZATION);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public String runLuceneIndex() throws IOException {
        indexService.init();
        int i = 0;
        for (PdfLink pdfLink:
                entitiesDao.get(
                        PdfLink.class,
                        Restrictions.or(Restrictions.isNull("indexedInLucene"),
                                Restrictions.eq("indexedInLucene", Boolean.FALSE)))) {
            indexService.addDocument(pdfLink);
            System.out.println("count "+i++);
            if (i == 1000){
                indexService.close();
                break;
            }
        }
        return null;
    }
}
