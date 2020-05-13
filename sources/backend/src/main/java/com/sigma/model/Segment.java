package com.sigma.model;

import javax.persistence.*;

import com.sigma.dto.SegmentDto;

import java.util.List;

@Entity
@Table(name = "Segment")
public class Segment {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String libelle;

  // @OneToOne
  private String cpv;

  // @OneToOne
  private String ape;

  @OneToMany
  private List<Metrique> metriques;
  
  @ManyToOne
  private Acheteur acheteur;

  public Segment(String libelle, String cpv, String ape, List<Metrique> metriques) {
    this.libelle = libelle;
    this.cpv = cpv;
    this.ape = ape;
    this.metriques = metriques;
  }
  public Segment(String libelle, String cpv, String ape) {
	  this.libelle = libelle;
	  this.cpv = cpv;
	  this.ape = ape;
  }
public Segment(SegmentDto segment) {
	
	
}
  public Segment() { }
  /*@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")*/
  public Long getId() {
	  return this.id;
  }
  public void setId(Long id) {
	  this.id = id ;
  }
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

  public void addMetrique(Metrique metrique) {
    this.metriques.add(metrique);
  }

  public void removeMetrique(Metrique metrique) {
    this.metriques.remove(metrique);
  }

  public void setMetrique(List<Metrique> metriques) {
    this.metriques = metriques;
  }

public Acheteur getAcheteur() {
	return acheteur;
}

public void setAcheteur(Acheteur acheteur) {
	this.acheteur = acheteur;
}
  
  
}