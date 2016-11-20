package com.kemi.service.lucene.impl;

import com.kemi.service.text.load.Loader;
import com.kemi.service.lucene.IndexService;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

    @PostConstruct
    public void init() throws IOException{
        //this directory will contain the indexes
        Directory indexDirectory =
                FSDirectory.open(new File(indexDirectoryPath));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig
                (LuceneConstants.VERSION,
                        new RussianAnalyzer(LuceneConstants.VERSION));
        //create the indexer
        writer = new IndexWriter(indexDirectory, indexWriterConfig);
    }

    @Override
    public void addDocument(String name) throws IOException {
        Document document = getDocument(name);
        writer.addDocument(document);
    }

    private Document getDocument(String name) throws IOException{
        Document document = new Document();

        Field contentField = new Field(name, loader.loadText(name), new FieldType());
        document.add(contentField);

        return document;
    }

    @PreDestroy
    public void close() throws IOException{
        writer.close();
    }
}