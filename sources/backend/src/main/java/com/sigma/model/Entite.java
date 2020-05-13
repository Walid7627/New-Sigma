package com.sigma.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Entite {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;


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

	@OneToOne(cascade = {CascadeType.ALL})
	private Address adresse;

	@OneToOne
	private AdministrateurEntite administrateur;
	
	@OneToMany(mappedBy="entite")
	private List<Visiteur> visiteur;

	@OneToMany(mappedBy="entite")
	private List<Equipe> equipes;

	@OneToMany(mappedBy="entite")
	@Fetch(value=FetchMode.SELECT)
	private List<Acheteur> membres;


	public Entite() {
		// TODO Auto-generated constructor stub
	}



	public Entite(String nomSociete, String numSiret, String logo, String codeAPE, String codeCPV, String raisonSociale,
				  FournisseurType typeEntreprise, String maisonMere, String siteInstitutionnel, String description, String mail,
				  String telephone, String fax, Address adresse, AdministrateurEntite administrateur) {

		this.numSiret = numSiret;
		this.logo = logo;
		this.nomSociete = nomSociete;
		this.codeAPE = codeAPE;
		this.codeCPV = codeCPV;
		this.raisonSociale = raisonSociale;
		this.typeEntreprise = typeEntreprise;
		this.maisonMere = maisonMere;
		this.siteInstitutionnel = siteInstitutionnel;
		this.description = description;
		this.mail = mail;
		this.telephone = telephone;
		this.fax = fax;
		this.adresse = adresse;
		this.administrateur = administrateur;
	}

	public Entite(String nomSociete, String numSiret, String logo, String codeAPE, String codeCPV, String raisonSociale,
				  FournisseurType typeEntreprise, String maisonMere, String siteInstitutionnel, String description, String mail,
				  String telephone, String fax, Address adresse) {

		this.numSiret = numSiret;
		this.logo = logo;
		this.nomSociete = nomSociete;
		this.codeAPE = codeAPE;
		this.codeCPV = codeCPV;
		this.raisonSociale = raisonSociale;
		this.typeEntreprise = typeEntreprise;
		this.maisonMere = maisonMere;
		this.siteInstitutionnel = siteInstitutionnel;
		this.description = description;
		this.mail = mail;
		this.telephone = telephone;
		this.fax = fax;
		this.adresse = adresse;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Equipe> getEquipes() {
		return equipes;
	}

	public void setEquipes(List<Equipe> equipes) {
		this.equipes = equipes;
	}

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

	public String getMaisonMere() { return maisonMere; }

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

	public AdministrateurEntite getAdministrateur() {
		return administrateur;
	}

	public void setAdministrateur(AdministrateurEntite administrateur) {
		this.administrateur = administrateur;
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

	public List<Acheteur> getMembres() {
		return membres;
	}


	public void setMembres(List<Acheteur> membres) {
		this.membres = membres;
	}

	public void addMembre(Acheteur membre) {
		this.membres.add(membre);
	}

	public void removeMembre(Acheteur membre) {
		this.membres.remove(membre);
	}
	
	public void addEquipe(Equipe equipe) {
		this.equipes.add(equipe);
	}

	public void removeEquipe(Equipe equipe) {
		this.equipes.remove(equipe);
	}


}
