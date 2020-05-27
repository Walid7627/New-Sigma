package com.sigma.dto;

import com.sigma.model.Acheteur;
import com.sigma.model.Metrique;

import java.util.List;



public class SegmentDto {
	private Long id;
	private String libelle;
	private String cpv;
	private List<Long>acheteurs;
	public List<Long> getAcheteurs() {
		return acheteurs;
	}

	public void setAcheteurs(List<Long> acheteurs) {
		this.acheteurs = acheteurs;
	}
	private String ape;
	private List<String> codesCPV;
	public List<String> getCodesCPV() {
		return codesCPV;
	}

	public void setCodesCPV(List<String> codesCPV) {
		this.codesCPV = codesCPV;
	}
	private List<Metrique> metriques;

	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getCodeCPV() {
		return this.cpv;
	}

	public void setCodeCPV(String cpv) {
		this.cpv = cpv;
	}

	public String getCodeAPE() {
		return this.ape;
	}

	public void setCodeAPE(String ape) {
		this.ape = ape;
	}

	public List<Metrique> getMetriques() {
		return this.metriques;
	}

	public void setMetriques(List<Metrique> metriques) {
		this.metriques = metriques;
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id=id;
	}

	
}