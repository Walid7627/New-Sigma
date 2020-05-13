package com.sigma.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.sigma.service.EntiteExcelService;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sigma.model.CodeAPE;
import com.sigma.model.Entite;
import com.sigma.repository.CodeAPERepository;
import com.sigma.service.StorageService;

@Component
public class EntiteExcelServiceImpl implements EntiteExcelService {
	private static String[] columns = {"N° Siret", "Nom", "Code APE", "Raison sociale", "Type Entreprise", "Maison Mère", 
			"Site Institutionnel", "description", "Email", "Telephone", "Fax", "Adresse", "Administrateur"};

	private String path = "files/entites-generated.xlsx";

	@SuppressWarnings("deprecation")
	@Override
	public String createFile(List<Entite> entities) throws IOException {
		// TODO Auto-generated method stub
		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("Entités");
		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		style.setFillForegroundColor(HSSFColor.BLUE.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font.setBold(true);
		font.setColor(HSSFColor.WHITE.index);
		style.setFont(font);

		Row headerRow = sheet.createRow(0);

		for(int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(style);
		}

		int rowNum = 1;

		for(Entite entite: entities) {
			Row row = sheet.createRow(rowNum++);
			String codeAPE = "";
			if (entite.getCodeAPE() != null) {
				CodeAPE code = codeAPERepository.findOne(entite.getCodeAPE());
				codeAPE = code.toString(); 
			}
			row.createCell(0).setCellValue(entite.getNumSiret());
			row.createCell(1).setCellValue(entite.getNomSociete());
			row.createCell(2).setCellValue(codeAPE);
			row.createCell(3).setCellValue(entite.getRaisonSociale());
			row.createCell(4).setCellValue(entite.getTypeEntreprise().toString());
			row.createCell(5).setCellValue(entite.getMaisonMere());
			row.createCell(6).setCellValue(entite.getSiteInstitutionnel());
			row.createCell(7).setCellValue(entite.getDescription());
			row.createCell(8).setCellValue(entite.getMail());
			row.createCell(9).setCellValue(entite.getTelephone());
			row.createCell(10).setCellValue(entite.getFax());
			row.createCell(11).setCellValue(entite.getAdresse().toString());
			String adm = (entite.getAdministrateur() != null) ? entite.getAdministrateur().getPrenom() + " " + entite.getAdministrateur().getNom() : "";
			row.createCell(12).setCellValue(adm);
		}

		for(int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the output to a file
		File file = storageService.get(path);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();

		return path;
	}

	@Autowired
	StorageService storageService;
	@Autowired
	private CodeAPERepository codeAPERepository;
}

