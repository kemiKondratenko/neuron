package com.kemi.storage.pdf.impl;

import com.ibm.icu.util.StringTokenizer;
import com.kemi.storage.pdf.PDFExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

/**
 * Created by SV on 21.04.2015.
 */
@Service
public class PDFExtractorImpl implements PDFExtractor {

    private String title;
    private String text;
    private String annotation;

    public PDFExtractorImpl() {
    }

    @Override
    public void setFile(String file) {
        extract(file);
    }

    private void extract(String file) {
        extractText(file);

        String text = getFirstPageText(file);
        BufferedReader reader = new BufferedReader(new StringReader(text));
        try {
            String str = reader.readLine();
            if (str != null)
                while (!str.toUpperCase().startsWith("УД"))
                    if ((str = reader.readLine()) == null) {
                        this.title = "[n/a]";
                        return;
                    }

            StringBuilder sb = new StringBuilder();
            do {
                str = reader.readLine();
                if (str != null && hasUppercaseWord(str))
                    sb.append(str);
                else break;
            } while (true);
            this.title = sb.toString().toUpperCase();

            sb = new StringBuilder();
            int count = 255;
            do {
                if (str == null) {
                    sb.append("...");
                    break;
                }
                if (count > str.length()) {
                    count -= str.length();
                    sb.append(str);
                } else {
                    sb.append(str.substring(0, count)).append("...");
                    count = 0;
                }
                str = reader.readLine();
            } while (count > 0);
            this.annotation = sb.toString();

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractText(String file) {
        try {
            PDDocument doc = PDDocument.load(new URL(file));
            PDFTextStripper stripper = new PDFTextStripper();
            this.text = stripper.getText(doc);
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFirstPageText(String file) {
        String text = "";
        try {
            PDDocument doc = PDDocument.load(new URL(file));
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setEndPage(1);
            text = stripper.getText(doc);
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    private boolean hasUppercaseWord(String str) {
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens())
            if (hasTwoUppercaseLetters(st.nextToken()))
                return true;
        return false;
    }

    private boolean hasTwoUppercaseLetters(String word) {
        byte count = 0;
        for (int i = 0; i < word.length(); ++i)
            if (Character.isUpperCase(word.charAt(i)))
                ++count;
        if (count > 1)
            return true;
        return false;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getAnnotation() {
        return annotation;
    }
}
