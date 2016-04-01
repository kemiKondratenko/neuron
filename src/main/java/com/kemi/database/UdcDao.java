package com.kemi.database;

import com.kemi.entities.UdcEntity;
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
public class UdcDao {

    @Autowired
    private EntitiesDao entitiesDao;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<UdcEntity> find(String href) {
        return entitiesDao.get(UdcEntity.class, Restrictions.eq("udc", href));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UdcEntity create(String href) {
        List<UdcEntity> forUrl = entitiesDao.get(UdcEntity.class, Restrictions.eq("udc", href));
        forUrl.remove(null);
        Integer i = 0;
        if(CollectionUtils.isEmpty(forUrl)) {
            UdcEntity pdfLink = new UdcEntity(href);
            i = entitiesDao.save(pdfLink);
            return entitiesDao.get(UdcEntity.class, i);
        }
        return forUrl.get(0);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public UdcEntity create(String href, Integer normalization) {
        List<UdcEntity> forUrl = entitiesDao.get(UdcEntity.class, Restrictions.eq("udc", href), Restrictions.eq("normalization", normalization));
        forUrl.remove(null);
        Integer i = 0;
        if(CollectionUtils.isEmpty(forUrl)) {
            UdcEntity pdfLink = new UdcEntity(href, normalization);
            i = entitiesDao.save(pdfLink);
            return entitiesDao.get(UdcEntity.class, i);
        }
        return forUrl.get(0);
    }

    public List<UdcEntity> getAll() {
        return entitiesDao.get(UdcEntity.class);
    }
}
