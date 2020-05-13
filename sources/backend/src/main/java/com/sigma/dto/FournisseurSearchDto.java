package com.sigma.dto;

public class FournisseurSearchDto {
  private String nom;
  private String prenom;
  private String nomSociete;
  private String mail;

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

  public String getNomSociete() {
    return this.nomSociete;
  }

  public void setNomSociete(String nomSociete) {
    this.nomSociete = nomSociete;
  }

  public String getMail() {
    return this.mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }
}
