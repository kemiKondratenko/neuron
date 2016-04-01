package com.kemi.udc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kemi.database.EntitiesDao;
import com.kemi.database.LinkToUdcDao;
import com.kemi.database.UdcDao;
import com.kemi.entities.LinkToUdc;
import com.kemi.entities.PdfLink;
import com.kemi.entities.UdcEntity;
import com.kemi.service.text.load.Loader;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Eugene on 27.03.2016.
 */
@Service
public class UdcFinder {

    @Autowired
    private EntitiesDao entitiesDao;
    @Autowired
    private UdcDao udcDao;
    @Autowired
    private LinkToUdcDao linkToUdcDao;
    @Autowired
    private Loader loader;

    public String index() {
        List<PdfLink> links = entitiesDao.get(PdfLink.class, Restrictions.isEmpty("linkToUdcs"));
        loadAndFindUdc(links);
        return null;
    }

    private void loadAndFindUdc(List<PdfLink> links) {
        for (PdfLink link : links) {
            loadAndFindUdc(link);
        }
    }

    private void loadAndFindUdc(PdfLink link) {
        List<LinkToUdc> linkToUdcs = linkToUdcDao.find(link);
        linkToUdcs.remove(null);
        if (CollectionUtils.isEmpty(linkToUdcs)) {
            String text = loader.loadText(link.getPdfLink());
            if (text != null) {
                String udcText = findUdcText(text);
                if (udcText != null) {
                    List<String> udcs = findUdc(udcText);
                    for (String udc : udcs) {
                        UdcEntity udcEntity = udcDao.create(udc);
                        createLink(link, udcEntity);
                    }
                }
            }
        }
    }

    private List<String> findUdc(String text) {
        List<String> res = Lists.newArrayList();
        StringBuilder resS = new StringBuilder("");
        for(int i = 0 ; i < text.length(); i++){
            char c = text.charAt(i);
            if(isNumber(c) || c == '.' || c == '+' || c == ':' || c == '/')
                resS.append(c);
            else if(c == ';'){
                if(StringUtils.isNotBlank(resS.toString()))
                    res.add(resS.toString());
                resS = new StringBuilder("");
            } else break;
        }
        if(StringUtils.isNotBlank(resS.toString()))
            res.add(resS.toString());
        return res;
    }

    private boolean isNumber(char c) {
        return (c >= '0' && c <= '9');
    }

    private void createLink(PdfLink link, UdcEntity udcEntity) {
        linkToUdcDao.create(link, udcEntity);
    }

    private String findUdcText(String text) {
        String res = null;
        int udc = text.indexOf("УДК");
        if (!(udc >= 0)) {
            udc = text.indexOf("удк");
        }
        if (udc >= 0) {
            res = text.substring(udc + 4, text.indexOf("\r\n", udc)).replace(" ", "");
        }
        return res;
    }

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
