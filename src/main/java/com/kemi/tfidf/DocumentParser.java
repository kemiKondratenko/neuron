package com.kemi.tfidf;

import com.kemi.database.EntitiesDao;
import com.kemi.entities.PdfLink;
import com.kemi.service.text.load.Loader;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Eugene on 23.03.2016.
 */
@Service
public class DocumentParser {

    @Autowired
    private EntitiesDao entitiesDao;
    @Autowired
    private Loader loader;
    @Autowired
    private ParseAndBuilder parseAndBuilder;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void parseFiles() {
        List<PdfLink> links = entitiesDao.get(PdfLink.class, Restrictions.eq("indexed", false));
        buildWordEntities(links);
    }

    private void buildWordEntities(List<PdfLink> links) {
        for (PdfLink link : links) {
            String text = loader.loadText(link.getPdfLink());
            parseAndBuilder.parseAndBuildWords(link, text);
        }
    }
}
