package com.sigma.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ContactRequest {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private long id;

  private String nom;
  private String telephone;
  private String mail;
  private String message;
  private String numSiret;
  private String nomSociete;

  public ContactRequest() { }

  public ContactRequest(String nom, String telephone, String mail, String message) {
    this.nom = nom;
    this.telephone = telephone;
    this.mail = mail;
    this.message = message;
  }

  public long getId() {
    return this.id;
  }

  public String getNom() {
    return this.nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getTelephone() {
    return this.telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getMail() {
    return this.mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

public String getNumSiret() {
	return numSiret;
}

public void setNumSiret(String numSiret) {
	this.numSiret = numSiret;
}

public String getNomSociete() {
	return nomSociete;
}

public void setNomSociete(String nomSociete) {
	this.nomSociete = nomSociete;
}
}
