package com.kemi.controllers;

import com.kemi.service.BuilderService;
import com.kemi.system.Sentence;
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


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "new", produces = "application/json;charset=utf-8")
    public @ResponseBody Collection<Sentence> getBook(){
        return builderService.get();
    }


}
