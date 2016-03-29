package com.kemi.tfidf;

import com.kemi.database.EntitiesDao;
import com.kemi.database.LinksDao;
import com.kemi.database.TextWordEntityDao;
import com.kemi.database.WordDao;
import com.kemi.entities.PdfLink;
import com.kemi.entities.WordEntity;
import com.kemi.service.text.load.Loader;
import org.apache.commons.lang3.StringUtils;
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
    private WordDao wordDao;
    @Autowired
    private TextWordEntityDao textWordEntityDao;
    @Autowired
    private LinksDao linksDao;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void parseFiles() {
        List<PdfLink> links = entitiesDao.get(PdfLink.class, Restrictions.eq("indexed", false));
        buildWordEntities(links);
    }

    private void buildWordEntities(List<PdfLink> links) {
        for (PdfLink link : links) {
            String text = loader.loadText(link.getPdfLink());
            parseAndBuildWords(link, text);
        }
    }

    private void parseAndBuildWords(PdfLink link, String text) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < text.length(); i++) {
            char c = Character.toLowerCase(text.charAt(i));
            if(
                    (c >= 'а' && c <= 'я')
                            || c == 'є'
                            || c == 'ї'
                            || c == 'і'
                            || c == 'ґ'
                    ){
                stringBuilder.append(c);
            } else {
                if (
                        (
                                c != '-'
                                && c != '\''
                                && c != '`'
                                && c != '’'
                        )
                        && StringUtils.isNotBlank(stringBuilder.toString())
                        ) {
                    if(stringBuilder.length() > 2) {
                        WordEntity wordE = wordDao.create(stringBuilder.toString());
                        textWordEntityDao.create(link, wordE);
                    }
                    stringBuilder = new StringBuilder("");
                }
            }
        }
        linksDao.indexed(link);
    }
}
