package com.kemi.database;

import com.kemi.entities.PdfLink;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Eugene on 27.03.2016.
 */
@Service
public class LinksDao {

    @Autowired
    private EntitiesDao entitiesDao;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public PdfLink create(String href) {
        List<PdfLink> forUrl = entitiesDao.get(PdfLink.class, Restrictions.eq("pdfLink", href));
        forUrl.remove(null);
        if(CollectionUtils.isEmpty(forUrl)) {
            PdfLink pdfLink = new PdfLink(href);
            entitiesDao.save(pdfLink);
            return pdfLink;
        }
        return forUrl.get(0);
    }

    public void indexed(PdfLink link) {
        link.setIndexed(true);
        entitiesDao.update(link);
    }

    public PdfLink get(int i) {
        return entitiesDao.get(PdfLink.class, Restrictions.eq("id", i)).get(0);
    }
}
