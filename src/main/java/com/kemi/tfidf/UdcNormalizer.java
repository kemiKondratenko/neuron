package com.kemi.tfidf;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kemi.database.LinkToUdcDao;
import com.kemi.database.UdcDao;
import com.kemi.entities.LinkToUdc;
import com.kemi.entities.UdcEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * Created by Eugene on 01.04.2016.
 */
@Service
public class UdcNormalizer {

    @Autowired
    private UdcDao udcDao;
    @Autowired
    private LinkToUdcDao linkToUdcDao;

    public String formNormalizedUdc(int chars) {
        Map<String, Set<UdcEntity>> normalizedUdcToIds = getNormilizedMap(chars);
        for (String normalizedUdc : normalizedUdcToIds.keySet()) {
            UdcEntity udcEntity = udcDao.create(normalizedUdc, chars);
            for (UdcEntity notNormalizedUdc : normalizedUdcToIds.get(normalizedUdc)) {
                for (LinkToUdc linkToUdc : notNormalizedUdc.getLinkToUdcs()) {
                    linkToUdcDao.create(linkToUdc.getPdfLink(), udcEntity);
                }
            }
        }
        return "";
    }

    private Map<String, Set<UdcEntity>> getNormilizedMap(int chars) {
        Map<String, Set<UdcEntity>> normilizedUdcToIds = Maps.newHashMap();
        for (UdcEntity udcEntity : udcDao.getAll()) {
            String normilizedUdc = udcEntity.getUdc().substring(0, chars);
            if (!normilizedUdcToIds.containsKey(normilizedUdc)){
                normilizedUdcToIds.put(normilizedUdc, Sets.<UdcEntity>newHashSet());
            }
            normilizedUdcToIds.get(normilizedUdc).add(udcEntity);
        }
        return normilizedUdcToIds;
    }
}
