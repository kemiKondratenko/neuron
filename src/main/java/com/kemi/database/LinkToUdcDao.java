package com.kemi.database;

import com.kemi.entities.LinkToUdc;
import com.kemi.entities.PdfLink;
import com.kemi.entities.UdcEntity;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Eugene on 27.03.2016.
 */
@Service
public class LinkToUdcDao {

    @Autowired
    private EntitiesDao entitiesDao;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<LinkToUdc> find(PdfLink link) {
        return entitiesDao.get(LinkToUdc.class, Restrictions.eq("pdfLink.id", link.getId()));
    }

    public void create(PdfLink link, UdcEntity udcEntity) {
        if(exist(link, udcEntity))
            return;
        LinkToUdc linkToUdc = new LinkToUdc(link, udcEntity);
        entitiesDao.save(linkToUdc);
    }

    private boolean exist(PdfLink link, UdcEntity udcEntity) {
        return !entitiesDao.get(LinkToUdc.class, Restrictions.eq("pdfLink.id", link.getId()),
                Restrictions.eq("udcEntity.id", udcEntity.getId())).isEmpty();
    }
}
