package com.kemi.storage;

import java.io.File;

/**
 * Created by KutsykV on 03.09.2015.
 */
public class Constants {

    public static final String linksFileName = "pdflinks.txt";
    public static final String linksFileLocalFolder = System.getProperty("C:") + File.separator + "links" + File.separator;

    private Constants(){
        throw new AssertionError();
    }

}
