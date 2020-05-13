package com.sigma.service;

import java.io.IOException;
import java.util.List;

import com.sigma.model.Entite;

public interface EntiteExcelService {
	String createFile(List<Entite> entities) throws IOException;


}
