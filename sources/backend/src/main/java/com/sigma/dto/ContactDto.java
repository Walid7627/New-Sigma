package com.sigma.dto;

public class ContactDto {

  private long id;

  private String nom;
  private String prenom;
  private String telephone;
  private String mobile;
  private String fax;
  private String mail;
  private String adresse;

  public ContactDto() { }

  public ContactDto(String nom, String prenom, String telephone,
  String mobile, String fax, String mail, String adresse) {
    this.prenom = prenom;
    this.nom = nom;
    this.telephone = telephone;
    this.mobile = mobile;
    this.fax = fax;
    this.mail = mail;
    this.adresse = adresse;

  }

  public long getId() {
    return this.id;
  }

  public String getPrenom() {
    return this.prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
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

  public String getMail() {
    return this.mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public String getAdresse() {
    return this.adresse;
  }

  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }
}
