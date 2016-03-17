package com.kemi.controllers;

import com.kemi.service.BuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Eugene on 16.03.2016.
 */
@Controller
@RequestMapping(value="/")
public class MainController {

    @Autowired
    BuilderService builderService;

    @RequestMapping(value = "new", produces = "text/plain;charset=UTF-8")
    public @ResponseBody String getBook(){
        return builderService.get();
    }


}
