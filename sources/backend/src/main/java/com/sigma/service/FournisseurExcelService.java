package com.sigma.service;

import java.io.IOException;
import java.util.List;

import com.sigma.model.Fournisseur;

public interface FournisseurExcelService {
	String createFile(List<Fournisseur> fournisseurs) throws IOException;


}
