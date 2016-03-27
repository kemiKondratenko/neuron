package com.kemi.controllers;

import com.kemi.entities.PdfLink;
import com.kemi.service.BuilderService;
import com.kemi.tfidf.DocumentParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collection;

/**
 * Created by Eugene on 16.03.2016.
 */
@Controller
@RequestMapping(value="/")
public class MainController {

    @Autowired
    BuilderService builderService;
    @Autowired
    DocumentParser documentParser;


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "new", produces = "application/json;charset=utf-8")
    public @ResponseBody Collection<String> getBook(){
/*
        try {
            documentParser.parseFiles("D:\\FolderToCalculateCosineSimilarityOf"); // give the location of source file
            documentParser.tfIdfCalculator(); //calculates tfidf
            documentParser.getCosineSimilarity();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return builderService.get();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "find", produces = "application/json;charset=utf-8")
    public @ResponseBody Collection<PdfLink> find(){
        return builderService.find();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "count", produces = "application/json;charset=utf-8")
    public @ResponseBody Number count(){
        return builderService.count();
    }



    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "findUdc", produces = "application/json;charset=utf-8")
    public @ResponseBody String findUdc(){
        return builderService.findUdc();
    }


}
