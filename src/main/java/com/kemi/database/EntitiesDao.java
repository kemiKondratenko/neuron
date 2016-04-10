package com.kemi.database;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Eugene on 27.03.2016.
 */
@Service
public class EntitiesDao {

    @Autowired
    private SessionFactory sessionFactory;

    public <T> int save(T publisher) {
        return (Integer) getSession().save(publisher);
    }

    public <T> T get(Class<T> tClass, int id){
        return (T) getSession().get(tClass, id);
    }

    public <T> List<T> get(Class<T> tClass){
        String fullClassName = tClass.getName();
        fullClassName = fullClassName.substring(fullClassName.lastIndexOf(".")+1, fullClassName.length());
        return getSession().createQuery("from "+fullClassName).list();
    }

    public <T> List<T> get(Class<T> authorEntityClass, Criterion... restrictions) {
        return get(authorEntityClass, 0, 0, restrictions);
    }

    public <T> List<T> get(Class<T> bookEntityClass, int from, int to, Criterion... restrictions) {
        Criteria criteria = getSession().createCriteria(bookEntityClass);
        criteria.setFirstResult(from);
        if (to != 0)
            criteria.setMaxResults(to);
        for (Criterion restriction : restrictions)
            criteria.add(restriction);
        return criteria.list();
    }


    public <T> Number count(Class<T> currentClass) {
        Criteria criteria = getSession().createCriteria(currentClass);
        criteria.setProjection(Projections.rowCount());
        return ((Number)criteria.uniqueResult());
    }

    private Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    public <T> T update(T object) {
        return (T) getSession().merge(object);
    }

    public <T> void delete(T link) {
        getSession().delete(link);
    }

    public <T> Number  executeCount(String s) {
        return ((Number) sessionFactory.getCurrentSession()
                .createSQLQuery(s)
                .uniqueResult()).longValue();
    }
}
