package com.kemi.controllers;

import com.kemi.entities.JsonDots;
import com.kemi.entities.UdcEntity;
import com.kemi.service.BuilderService;
import com.kemi.tfidf.DocumentParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collection;
import java.util.List;

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
    public @ResponseBody String getBook(){
        builderService.get();
        return "ok";
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "find", produces = "application/json;charset=utf-8")
    public @ResponseBody Collection<UdcEntity> find(){
        return builderService.find();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "count", produces = "application/json;charset=utf-8")
    public @ResponseBody Number count(){
        return builderService.count();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "udcCount", produces = "application/json;charset=utf-8")
    public @ResponseBody Number udcCount(){
        return builderService.udcCount();
    }



    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "findUdc", produces = "application/json;charset=utf-8")
    public @ResponseBody String findUdc(){
        return builderService.findUdc();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "index", produces = "application/json;charset=utf-8")
    public @ResponseBody String index(){
        return builderService.index();
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "ctf", produces = "application/json;charset=utf-8")
    public @ResponseBody String ctf(){
        return builderService.ctf();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "cidf", produces = "application/json;charset=utf-8")
    public @ResponseBody String cidf(){
        return builderService.cidf();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "loadDots", produces = "application/json;charset=utf-8")
    public @ResponseBody
    List<JsonDots> loadDots(){
        return builderService.getDots();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "formNormalizedUdc", produces = "application/json;charset=utf-8")
    public @ResponseBody
    String formNormilizedUdc(){
        return builderService.formNormalizedUdc();
    }


}
