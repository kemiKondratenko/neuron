package com.kemi.clastering;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kemi.database.EntitiesDao;
import com.kemi.database.LinksDao;
import com.kemi.database.UdcDao;
import com.kemi.entities.LinkToUdc;
import com.kemi.entities.PdfLink;
import com.kemi.entities.UdcEntity;
import com.kemi.entities.mongo.NormalizedUdcMongoEnity;
import com.kemi.entities.mongo.TextWordMongoEntity;
import com.kemi.entities.mongo.WordMongoEntity;
import com.kemi.mongo.MongoBase;
import com.kemi.service.text.load.Loader;
import com.kemi.tfidf.ParseAndBuilder;
import com.kemi.tfidf.TfIdf;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eugene on 05.04.2016.
 */
@Service
public class Cluster {

    @Autowired
    private ParseAndBuilder parseAndBuilder;
    @Autowired
    private Loader loader;
    @Autowired
    private MongoBase mongoBase;
    @Autowired
    private TfIdf tfIdf;
    @Autowired
    private LinksDao linksDao;
    @Autowired
    private EntitiesDao entitiesDao;
    @Autowired
    private UdcDao udcDao;

    public Map<String, Double> cluster() {
        Integer correct = 0;
        Integer inCorrect = 0;
        List<PdfLink> links = entitiesDao.get(PdfLink.class,
                Restrictions.eq("indexed", true));


        List<PdfLink> remove = Lists.newArrayList();
        for (PdfLink link : links) {
            for (LinkToUdc linkToUdc : link.getLinkToUdcs()) {
                if(Integer.valueOf(1).equals(linkToUdc.getUdcEntity().getNormalization())){
                    remove.add(link);
                    break;
                }
            }
        }
        links.removeAll(remove);
        final Integer all = links.size();
        for (PdfLink pdfLink : links) {
            Map<String, Double> result = getByNaiveBaise(pdfLink);
            //Map<String, Double> result2 = getResultKNN(pdfLink);
            Map<String, Double> result3 = getSimple(pdfLink);
            String max = getMax(result);
           // String max2 = getMax(result2);
            String max3 = getMax(result3);
            if(!max3.equals(max)){
                max = max3;
            }/* else if(max3.equals(max2)){
                max = max2;
            } else if(max2.equals(max)){
                max = max2;
            }*/ else {
                max = max3;
            }
            if(correct(max, pdfLink) && StringUtils.isNotBlank(max)){
                correct++;
            } else {
                inCorrect++;
            }
        }
        final Double correctP = correct / (all/100.);
        final Double inCorrectP = inCorrect / (all/100.);
        return new HashMap<String, Double>()
        {{put("correct", correctP);put("inCorrect", inCorrectP);}};
    }

    private boolean correct(String max, PdfLink pdfLink) {
        boolean correctB = false;
        for (LinkToUdc linkToUdc : pdfLink.getLinkToUdcs()) {
            if(linkToUdc.getUdcEntity().getUdc().startsWith(max)){
                correctB = true;
                break;
            }
        }
        return correctB;
    }

    private String getMax(Map<String, Double> result) {
        String res = "";
        Double resD = Double.NEGATIVE_INFINITY;
        for (Map.Entry<String, Double> stringDoubleEntry : result.entrySet()) {
            if(stringDoubleEntry.getValue() > resD){
                res = stringDoubleEntry.getKey();
                resD = stringDoubleEntry.getValue();
            }
        }
        return res;
    }

    public Map<String,Double> getResultKNN(PdfLink pdfLink) {
        Double magnitude1 = 0.0;
        Map<Integer, Double> dotProduct = Maps.newHashMap();
        Map<Integer, Double> magnitude2 = Maps.newHashMap();
        for (TextWordMongoEntity textWordMongoEntity : mongoBase.getPdfLinkTerms(pdfLink.getId())) {
            WordMongoEntity wordMongoEntity = mongoBase.getWord(textWordMongoEntity.getWordEntity());
            if (wordMongoEntity.getIdf() != null & textWordMongoEntity.getTf() != null) {
                Double docVector1 = wordMongoEntity.getIdf() * textWordMongoEntity.getTf();
                magnitude1 += Math.pow(docVector1, 2);
                for (NormalizedUdcMongoEnity normalizedUdcMongoEnity : mongoBase.getNormalizedAndUniqueUdcTerms(wordMongoEntity.getId())) {
                    Double docVector2 = Math.pow(normalizedUdcMongoEnity.getCommon(), 2);
                    if (!dotProduct.containsKey(normalizedUdcMongoEnity.getNormalizedUdc())) {
                        magnitude2.put(normalizedUdcMongoEnity.getNormalizedUdc(), Double.valueOf(0));
                        dotProduct.put(normalizedUdcMongoEnity.getNormalizedUdc(), Double.valueOf(0));
                    }
                    Double aDouble = magnitude2.get(normalizedUdcMongoEnity.getNormalizedUdc()) + docVector2;
                    magnitude2.put(normalizedUdcMongoEnity.getNormalizedUdc(), aDouble);
                    Double aDouble2 = dotProduct.get(normalizedUdcMongoEnity.getNormalizedUdc()) + docVector1 * docVector2;
                    dotProduct.put(normalizedUdcMongoEnity.getNormalizedUdc(), aDouble2);
                }
            } else {
                System.out.println();
            }
        }
        magnitude1 = Math.sqrt(magnitude1);
        Map<String, Double> result = Maps.newHashMap();
        for (Integer integer : dotProduct.keySet()) {
            magnitude2.put(integer, Math.sqrt(magnitude2.get(integer)));
            if (magnitude1 != 0.0 || magnitude2.get(integer) != 0.0) {
                result.put(udcDao.get(integer).getUdc(), dotProduct.get(integer) / (magnitude1 * magnitude2.get(integer)));
            }
        }
        return result;
    }


    public Map<String,Double> getSimple(PdfLink pdfLink) {
        Map<Integer, Double> resultI = Maps.newHashMap();
        for (TextWordMongoEntity textWordMongoEntity : mongoBase.getPdfLinkTerms(pdfLink.getId())) {
            WordMongoEntity wordMongoEntity = mongoBase.getWord(textWordMongoEntity.getWordEntity());
            for (NormalizedUdcMongoEnity normalizedUdcMongoEnity : mongoBase.getNormalizedAndUniqueUdcTerms(wordMongoEntity.getId())) {
                if (!resultI.containsKey(normalizedUdcMongoEnity.getNormalizedUdc())) {
                    resultI.put(normalizedUdcMongoEnity.getNormalizedUdc(), Double.valueOf(0));
                }
                Double aDouble = resultI.get(normalizedUdcMongoEnity.getNormalizedUdc()) + textWordMongoEntity.getCount()*normalizedUdcMongoEnity.getCommon();
                resultI.put(normalizedUdcMongoEnity.getNormalizedUdc(), aDouble);
            }
        }
        Map<String, Double> result = Maps.newHashMap();
        for (Integer integer : resultI.keySet()) {
            result.put(udcDao.get(integer).getUdc(), resultI.get(integer));
        }
        return result;
    }


    public Map<String,Double> getByNaiveBaise(PdfLink pdfLink) {
        Map<Integer, Double> resultI = Maps.newHashMap();
        for (TextWordMongoEntity textWordMongoEntity : mongoBase.getPdfLinkTerms(pdfLink.getId())) {
            for (NormalizedUdcMongoEnity normalizedUdcMongoEnity : mongoBase.getNormalizedAndUniqueUdcTerms(textWordMongoEntity.getWordEntity())) {
                if (!resultI.containsKey(normalizedUdcMongoEnity.getNormalizedUdc())) {
                    resultI.put(normalizedUdcMongoEnity.getNormalizedUdc(), Double.valueOf(0));
                }
                Double aDouble = resultI.get(normalizedUdcMongoEnity.getNormalizedUdc()) + /*Math.log(*/textWordMongoEntity.getCount()*normalizedUdcMongoEnity.getTf()/*)*/;
                resultI.put(normalizedUdcMongoEnity.getNormalizedUdc(), aDouble);
            }
        }
        Map<String, Double> result = Maps.newHashMap();
        for (Integer integer : resultI.keySet()) {
            UdcEntity udcEntity = udcDao.get(integer);
            //Double pC = Math.log(udcEntity.getPossibilityOfUdc());
            result.put(udcDao.get(integer).getUdc(), /*pC + */resultI.get(integer));
        }
        return result;
    }
}
