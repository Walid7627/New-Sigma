package com.sigma.model;

import javax.persistence.*;

@Entity
@Table(name = "metrique")
public class Metrique {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String libelle;
    private int note;
    // private Acheteur acheteur;

    public Metrique() {
    }

    public Metrique(String libelle, int note) {
        this.libelle = libelle;
        this.note = note;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getNote() {
        return this.note;
    }

    public void setNote(int n) {

    }

    // public Acheteur getAcheteur() {
    //   return this.acheteur;
    // }
    //
    // public void setAcheteur(Acheteur acheteur) {
    //   this.acheteur = acheteur;
    // }
}
