package com.kemi.tfidf;

import com.kemi.database.LinksDao;
import com.kemi.database.TextWordEntityDao;
import com.kemi.database.WordDao;
import com.kemi.entities.PdfLink;
import com.kemi.entities.WordEntity;
import com.kemi.mongo.MongoBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Eugene on 30.03.2016.
 */
@Service
public class ParseAndBuilder {
    @Autowired
    private WordDao wordDao;
    @Autowired
    private LinksDao linksDao;
    @Autowired
    private MongoBase mongoBase;
    private static  int one = 0;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void parseAndBuildWords(PdfLink link, String text) {
        if (text != null) {
            StringBuilder stringBuilder = new StringBuilder("");
            for (int i = 0; i < text.length(); i++) {
                char c = Character.toLowerCase(text.charAt(i));
                if (
                        (c >= 'а' && c <= 'я')
                                || c == 'є'
                                || c == 'ї'
                                || c == 'і'
                                || c == 'ґ'
                        ) {
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
                        if (stringBuilder.length() > 2) {
                            one++;
                            WordEntity wordE = wordDao.create(stringBuilder.toString());
                            mongoBase.create(link, wordE);
                        }
                        stringBuilder = new StringBuilder("");
                    }
                }
            }
            linksDao.indexed(link);
        }
    }
}
