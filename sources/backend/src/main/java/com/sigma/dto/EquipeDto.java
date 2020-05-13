package com.sigma.dto;

import com.sigma.model.Acheteur;
import com.sigma.model.Entite;
import com.sigma.model.ResponsableAchat;

import java.util.List;



public class EquipeDto {
  private Long responsable;

  private String libelle;

  private Long entite;

  private List<Long> membres;

  public String getLibelle() {
    return this.libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public Long getResponsable() {
    return this.responsable;
  }

  public void setResponsable(Long responsable) {
    this.responsable = responsable;
  }

  public List<Long> getMembres() {
    return this.membres;
  }

  public void setMembres(List<Long> membres) {
    this.membres = membres;
  }

  public Long getEntity() { return this.entite; }

  public void setEntite(Long entity) { this.entite = entity; }
}