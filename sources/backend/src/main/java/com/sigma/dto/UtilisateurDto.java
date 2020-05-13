package com.sigma.dto;

import java.util.Date;

import com.sigma.model.Address;

public class UtilisateurDto {
	
    private Long id;
    private String nom;
    private String prenom;
    private String mail;
    private Address adresse;
    private String password;
    private Date dateEnregistrement;
    private String userType;
    private String telephone;
    private String mobile;


    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Address getAdresse() {
        return this.adresse;
    }

    public void setAdresse(Address adresse) {
        this.adresse = adresse;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateEnregistrement() {
        return this.dateEnregistrement;
    }

    public void setDateEnregistrement(Date dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String type) {
        this.userType = type;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
