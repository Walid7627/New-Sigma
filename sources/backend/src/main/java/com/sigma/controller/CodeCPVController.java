package com.sigma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigma.model.CodeCPV;
import com.sigma.repository.CodeCPVRepository;



@RestController
@RequestMapping("/api/codecpv")
public class CodeCPVController {

	@Autowired
	private CodeCPVRepository codeCpvRepository;

	@GetMapping
	public List<CodeCPV> getApes(){
		return codeCpvRepository.findAll();
	}

}
