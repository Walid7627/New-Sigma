package com.sigma.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
import com.sigma.model.CodeCPV;
import com.sigma.model.Fournisseur;
import com.sigma.repository.CodeAPERepository;
import com.sigma.repository.CodeCPVRepository;
import com.sigma.repository.FournisseurRepository;
import com.sigma.service.FournisseurExcelService;
import com.sigma.service.StorageService;

@Component
public class FournisseurExcelServiceImpl  implements FournisseurExcelService {
	private static String[] columns = {"N° Siret", "Nom", "Code APE","Codes CPV", "Raison sociale", "Type Entreprise", "Maison Mère", 
			"Site Institutionnel", "description", "Email", "Telephone", "Mobile","Fax", "Adresse", "Gestionnaire"};
	
	private String path = "files/providers-generated.xlsx";

	@SuppressWarnings("deprecation")
	public String createFile(List<Fournisseur> fournisseurs) throws IOException {
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
		
        for(Fournisseur fournisseur: fournisseurs) {
        	Row row = sheet.createRow(rowNum++);
        	String maisonMere = "";
        	if (fournisseur.getMaisonMere() != null) {
        		Fournisseur m = fournisseurRepository.findOne(fournisseur.getMaisonMere());
        		maisonMere = m.getNomSociete() + " (" + m.getNumSiret() + ")";
        	}
        	
        	String codeAPE = "";
        	if (fournisseur.getCodeAPE() != null) {
        		CodeAPE code = codeAPERepository.findOne(fournisseur.getCodeAPE());
        		codeAPE = code.toString(); 
        	}
        	
        	
        	String codeCPV = "";
        	if (fournisseur.getCodeCPV() != null) {
        		for (String c : fournisseur.getCodeCPV()) {
        			CodeCPV ca = codeCPVRepository.findOne(c);
        			if(ca != null) {
        				if(!codeCPV.isEmpty()) {
        					codeCPV += " | ";
        				}
        				codeCPV += ca.toString();
        			}
        		}
        	}
        	
        	row.createCell(0).setCellValue(fournisseur.getNumSiret());
        	row.createCell(1).setCellValue(fournisseur.getNomSociete());
        	row.createCell(2).setCellValue(codeAPE);
        	row.createCell(3).setCellValue(codeCPV);
        	row.createCell(4).setCellValue(fournisseur.getRaisonSociale());
        	row.createCell(5).setCellValue(fournisseur.getTypeEntreprise().toString());
        	row.createCell(6).setCellValue(maisonMere);
        	row.createCell(7).setCellValue(fournisseur.getSiteInstitutionnel());
        	row.createCell(8).setCellValue(fournisseur.getDescription());
        	row.createCell(9).setCellValue(fournisseur.getMail());
        	row.createCell(10).setCellValue(fournisseur.getTelephone());
        	row.createCell(11).setCellValue(fournisseur.getMobile());
        	row.createCell(12).setCellValue(fournisseur.getFax());
        	row.createCell(13).setCellValue(fournisseur.getAdresse().toString());
        	String ger = fournisseur.getPrenom() + " " + fournisseur.getNom();
        	row.createCell(14).setCellValue(ger);
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
	private FournisseurRepository fournisseurRepository;
	
	@Autowired
	private CodeAPERepository codeAPERepository;
	
	@Autowired
	private CodeCPVRepository codeCPVRepository;
}

