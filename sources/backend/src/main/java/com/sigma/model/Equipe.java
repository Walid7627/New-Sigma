package com.sigma.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sigma.repository.AcheteurRepository;
import com.sigma.repository.EntiteRepository;
import com.sigma.repository.ResponsableAchatRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Equipe {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne
  private Acheteur responsable;

  private String libelle;

  @OneToMany(mappedBy = "equipe")
  private List<Acheteur> membres;

  @ManyToOne
  private Entite entite;

  public Equipe(String libelle, Acheteur responsable, Entite entite, List<Acheteur> acheteurs) {
    this.libelle = libelle;
    this.responsable = responsable;
    this.entite = entite;
    this.membres = acheteurs;
  }

  public Equipe() {
  }

  public Long getId() {
    return this.id;
  }

  public String getLibelle() {
    return this.libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public Acheteur getResponsable() {return this.responsable;}

  public void setResponsable(Acheteur responsable) {
    this.responsable = responsable;
  }

  public List<Acheteur> getMembres() {
    return this.membres;
  }

  public void addMembre(Acheteur membre) {
    this.membres.add(membre);
  }

  public void removeMembre(Acheteur membre) {
    this.membres.remove(membre);
  }

  public Entite getEntite() { return entite; }

  public void setEntite(Entite entite) { this.entite = entite; }
}