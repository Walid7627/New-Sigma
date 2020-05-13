package com.sigma.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageNotFoundController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
      // return "forward:/index.html";
      return "";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
