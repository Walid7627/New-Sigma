package com.sigma.dto;

import com.sigma.model.Document;
import com.sigma.model.FournisseurType;
import com.sigma.model.Metrique;

import java.util.List;


public class FournisseurDto extends UtilisateurDto {
	private Long id;
	private String numSiret;
	private String logo;
	private String nomSociete;
	private String codeAPE;
	private List<String> codeCPV;
	private String raisonSociale;
	private String siteInstitutionnel;
	private String description;
	private String telephone;
	private String mobile;
	private String fax;
	// private List<Document> documents;
	// private Qualification qualifications;
	private List<Metrique> evaluations;
	private FournisseurType typeEntreprise;
	private Long maisonMere;
	private List<Document> documents;


	public String getNumSiret() {
		return this.numSiret;
	}

	public void setNumSiret(String siret) {
		this.numSiret = siret;
	}

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getNomSociete() {
		return this.nomSociete;
	}

	public void setNomSociete(String nom) {
		this.nomSociete = nom;
	}

	public String getCodeAPE() {
		return this.codeAPE;
	}

	public void setCodeAPE(String code) {
		this.codeAPE = code;
	}

	public List<String> getCodeCPV() {
		return this.codeCPV;
	}

	public void setCodeCPV(List<String> code) {
		this.codeCPV = code;
	}

	public String getRaisonSociale() {
		return this.raisonSociale;
	}

	public void setRaisonSociale(String raisonSociale) {
		this.raisonSociale = raisonSociale;
	}

	public String getSiteInstitutionnel() {
		return this.siteInstitutionnel;
	}

	public void setSiteInstitutionnel(String siteInstitutionnel) {
		this.siteInstitutionnel = siteInstitutionnel;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Metrique> getEvaluations() {
		return this.evaluations;
	}

	public void setEvaluation(List<Metrique> evaluations) {
		this.evaluations = evaluations;
	}

	public FournisseurType getTypeEntreprise() {
		return this.typeEntreprise;
	}

	public void setTypeEntreprise(FournisseurType type) {
		this.typeEntreprise = type;
	}

	public Long getMaisonMere() {
		return this.maisonMere;
	}

	public void setMaisonMere(Long maisonMere) {
		this.maisonMere = maisonMere;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public List<Document> getDocuments() {
		return this.documents;
	}

	public void addDocument(Document d) {
		this.documents.add(d);
	}

	public void updateDocument(Document d) {
		if (this.documents.contains(d)) {
			this.removeDocument(d);
		}

		this.addDocument(d);
	}

	public void removeDocument(Document d) {
		this.documents.remove(d);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
