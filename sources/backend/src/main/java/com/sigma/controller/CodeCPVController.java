package com.sigma.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sigma.model.ApiResponse;
import com.sigma.model.CodeCPV;
import com.sigma.model.Fournisseur;
import com.sigma.repository.CodeCPVRepository;
import com.sigma.util.IterableToList;



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
