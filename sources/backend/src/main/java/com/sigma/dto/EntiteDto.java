package com.sigma.dto;

import java.util.List;

import com.sigma.model.Address;
import com.sigma.model.AdministrateurEntite;
import com.sigma.model.Equipe;
import com.sigma.model.FournisseurType;

public class EntiteDto {
	private Long id;
	private String numSiret;
	private String logo;
	private String nomSociete;
	private String codeAPE;
	private String codeCPV;
	private String raisonSociale;
	private FournisseurType typeEntreprise;
	private String maisonMere;
	private String siteInstitutionnel;
	private String description;
	private String mail;
	private String telephone;
	private String fax;
	private Address adresse;
	private AdministrateurEntite administrateur;
	private List<Equipe> equipes;
	public String getNumSiret() {
		return numSiret;
	}
	public void setNumSiret(String numSiret) {
		this.numSiret = numSiret;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getNomSociete() {
		return nomSociete;
	}
	public void setNomSociete(String nomSociete) {
		this.nomSociete = nomSociete;
	}
	public String getCodeAPE() {
		return codeAPE;
	}
	public void setCodeAPE(String codeAPE) {
		this.codeAPE = codeAPE;
	}
	public String getCodeCPV() {
		return codeCPV;
	}
	public void setCodeCPV(String codeCPV) {
		this.codeCPV = codeCPV;
	}
	public String getRaisonSociale() {
		return raisonSociale;
	}
	public void setRaisonSociale(String raisonSociale) {
		this.raisonSociale = raisonSociale;
	}
	public FournisseurType getTypeEntreprise() {
		return typeEntreprise;
	}
	public void setTypeEntreprise(FournisseurType typeEntreprise) {
		this.typeEntreprise = typeEntreprise;
	}
	public String getMaisonMere() {
		return maisonMere;
	}
	public void setMaisonMere(String maisonMere) {
		this.maisonMere = maisonMere;
	}
	public String getSiteInstitutionnel() {
		return siteInstitutionnel;
	}
	public void setSiteInstitutionnel(String siteInstitutionnel) {
		this.siteInstitutionnel = siteInstitutionnel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public Address getAdresse() {
		return adresse;
	}
	public void setAdresse(Address adresse) {
		this.adresse = adresse;
	}
	public AdministrateurEntite getAdministrateur() {
		return administrateur;
	}
	public void setAdministrateur(AdministrateurEntite administrateur) {
		this.administrateur = administrateur;
	}
	public List<Equipe> getEquipes() {
		return equipes;
	}
	public void setEquipes(List<Equipe> equipes) {
		this.equipes = equipes;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
}
