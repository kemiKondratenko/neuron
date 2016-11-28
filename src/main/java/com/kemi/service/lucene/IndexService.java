package com.kemi.service.lucene;

import com.kemi.entities.PdfLink;

import java.io.IOException;

/**
 * Created by Eugene on 20.11.2016.
 */
public interface IndexService {
    void addDocument(PdfLink name) throws IOException;

    void close() throws IOException;

    void init() throws IOException;
}
