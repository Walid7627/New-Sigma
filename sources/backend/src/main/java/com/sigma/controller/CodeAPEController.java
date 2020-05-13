package com.sigma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigma.model.CodeAPE;
import com.sigma.repository.CodeAPERepository;

@RestController
@RequestMapping("/api/codeape")
public class CodeAPEController {

    @Autowired
    private CodeAPERepository codeApeRepository;
    
    @GetMapping
    public List<CodeAPE> getApes() {
        return codeApeRepository.findAll();
    }
    
    @GetMapping("/{code}")
    public CodeAPE getApeByCode(@PathVariable String code) {
    	CodeAPE codeApe = codeApeRepository.findOne(code);
    	return codeApe;
    }

}
