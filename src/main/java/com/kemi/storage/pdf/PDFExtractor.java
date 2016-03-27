package com.kemi.storage.pdf;

/**
 * Created by KutsykV on 23.09.2015.
 */
public interface PDFExtractor {
    void setFile(String file);
    String getTitle();
    String getText();
    String getAnnotation();
}
