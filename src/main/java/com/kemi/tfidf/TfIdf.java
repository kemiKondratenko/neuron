package com.kemi.tfidf;

import com.google.common.collect.Lists;
import com.kemi.database.EntitiesDao;
import com.kemi.entities.PdfLink;
import com.kemi.entities.mongo.TextWordMongoEntity;
import com.kemi.entities.mongo.WordMongoEntity;
import com.kemi.mongo.MongoBase;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Eugene on 23.03.2016.
 */
@Service
public class TfIdf {

    @Autowired
    private EntitiesDao entitiesDao;

    @Autowired
    private MongoBase mongoBase;

    /**
     * Calculates the tf of term termToCheck
     * @param totalterms : Array of all the words under processing document
     * @param termToCheck : term of which tf is to be calculated.
     * @return tf(term frequency) of term termToCheck
     */
    public double tfCalculator(String[] totalterms, String termToCheck) {
        double count = 0;  //to count the overall occurrence of the term termToCheck
        for (String s : totalterms) {
            if (s.equalsIgnoreCase(termToCheck)) {
                count++;
            }
        }
        return count / totalterms.length;
    }

    /**
     * Calculates idf of term termToCheck
     * @param allTerms : all the terms of all the documents
     * @param termToCheck
     * @return idf(inverse document frequency) score
     */
    public double idfCalculator(List<String[]> allTerms, String termToCheck) {
        double count = 0;
        for (String[] ss : allTerms) {
            for (String s : ss) {
                if (s.equalsIgnoreCase(termToCheck)) {
                    count++;
                    break;
                }
            }
        }
        return 1 + Math.log(allTerms.size() / count);
    }



    /**
     * Method to create termVector according to its tfidf score.
     * @param termsDocsArray
     * @param allTerms
     */
    public List<Double[]> tfIdfCalculator(List<String[]> termsDocsArray, String[] allTerms) {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency
        List<Double[]> tfidfDocsVector = Lists.newArrayList();
        for (String[] docTermsArray : termsDocsArray) {
            Double[] tfidfvectors = new Double[allTerms.length];
            int count = 0;
            for (String terms : allTerms) {
                tf = tfCalculator(docTermsArray, terms);
                idf = idfCalculator(termsDocsArray, terms);
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;
                count++;
            }
            tfidfDocsVector.add(tfidfvectors);  //storing document vectors;
        }
        return tfidfDocsVector;
    }

    public String ctf() {
        for (PdfLink pdfLink : entitiesDao.get(PdfLink.class, Restrictions.eq("indexed", true))) {
            ctf(pdfLink.getId());
        }
        return "";
    }

    public String cidf() {
        Double wordsCount = Double.valueOf(mongoBase.getWordsCount());
        for (WordMongoEntity wordMongoEntity : mongoBase.get(WordMongoEntity.class)) {
            Double wordCount = Double.valueOf(mongoBase.getWordsCount(wordMongoEntity.getId()));
            wordMongoEntity.setIdf(1 + Math.log(wordsCount / wordCount));
        }
        return "";
    }

    private void ctf(int id) {
        int docTerms = mongoBase.getPdfLinkTermsAmount(id);
        if(docTerms > 0) {
            for (TextWordMongoEntity textWordMongoEntity : mongoBase.getPdfLinkTerms(id)) {
                textWordMongoEntity.setTf(Double.valueOf(textWordMongoEntity.getCount()) / Double.valueOf(docTerms));
                mongoBase.save(textWordMongoEntity);
            }
        }
    }
}
