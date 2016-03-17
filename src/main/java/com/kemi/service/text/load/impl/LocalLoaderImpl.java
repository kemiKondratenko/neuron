package com.kemi.service.text.load.impl;

import com.kemi.service.text.load.Loader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Eugene on 17.03.2016.
 */
@Service
public class LocalLoaderImpl implements Loader {


    @Override
    public String loadText(String fileName) {
        StringBuffer res = new StringBuffer();
        try(
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        this.getClass().getResourceAsStream
                                (fileName), "UTF8"))
        ) {
            String str = "";
            do {
                res.append(str);
                str = in.readLine();
            } while (str != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }
}
