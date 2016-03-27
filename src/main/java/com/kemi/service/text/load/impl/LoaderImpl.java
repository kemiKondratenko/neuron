package com.kemi.service.text.load.impl;

import com.kemi.service.text.load.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Eugene on 17.03.2016.
 */
@Service
public class LoaderImpl implements Loader {


    @Override
    public String loadText(String file) {
        String text = null;
        try {
            PDDocument doc = PDDocument.load(new URL(file));
            PDFTextStripper stripper = new PDFTextStripper();
            text = stripper.getText(doc);
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return text;
    }
}
