package com.kemi.service.lucene.impl;

import com.kemi.database.EntitiesDao;
import com.kemi.entities.PdfLink;
import com.kemi.entities.solr.UdcDocument;
import com.kemi.service.lucene.IndexService;
import com.kemi.service.text.load.Loader;
import com.kemi.solr.SolrDocumentRepository;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

@Service
public class IndexServiceImpl implements IndexService {

    private IndexWriter writer;

    @Autowired
    @Qualifier("indexDirectory")
    private String indexDirectoryPath;

    @Autowired
    private Loader loader;
    @Autowired
    private EntitiesDao entitiesDao;
    @Autowired
    private SolrDocumentRepository solrDocumentRepository;


    @PostConstruct
    public void init() throws IOException{
        Directory indexDirectory =
                FSDirectory.open(new File(indexDirectoryPath));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig
                (LuceneConstants.VERSION,
                        new RussianAnalyzer(LuceneConstants.VERSION));
        if (writer == null) {
            writer = new IndexWriter(indexDirectory, indexWriterConfig);
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void addDocumentToSolr(PdfLink pdfLink) {
        String text = loader.loadText(pdfLink.getPdfLink());
        if (text == null){
            return;
        }
        UdcDocument document = new UdcDocument();
        document.setName(pdfLink.getPdfLink());
        document.setText(text);
        solrDocumentRepository.save(document);
        pdfLink.setIndexedInSolr(Boolean.TRUE);
        entitiesDao.update(pdfLink);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void addDocument(PdfLink pdfLink) throws IOException {
        Document document = getDocument(pdfLink.getPdfLink());
        if(document != null) {
            writer.addDocument(document);
            writer.prepareCommit();
            writer.commit();
        }
        pdfLink.setIndexedInLucene(Boolean.TRUE);
        entitiesDao.update(pdfLink);
    }

    private Document getDocument(String name) throws IOException{
        String text = loader.loadText(name);
        if (text == null){
            return null;
        }
        Document document = new Document();

        FieldType fieldType = new FieldType();
        fieldType.setIndexed(true);
        fieldType.setStored(true);
        Field contentField = new Field(LuceneConstants.CONTENTS,
                text, fieldType);
        document.add(contentField);

        FieldType fieldType2 = new FieldType();
        fieldType2.setStored(true);
        Field nameField = new Field(LuceneConstants.FILE_NAME,
                name, fieldType2);
        document.add(nameField);

        return document;
    }

    @PreDestroy
    public void close() throws IOException{
        writer.prepareCommit();
        writer.commit();
        writer.close();
        writer = null;
    }
}