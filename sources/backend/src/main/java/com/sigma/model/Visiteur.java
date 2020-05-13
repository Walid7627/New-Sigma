package com.sigma.model;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.sigma.utilisateur.Utilisateur;

@Entity
@DiscriminatorValue("VI")
public class Visiteur extends Utilisateur {
	
	@ManyToOne
	private Entite entite;
	
	
	public Visiteur() {
		// TODO Auto-generated constructor stub
	}
	public Visiteur(String nom, String prenom, String mail, Address adresse, String telephone, String mobile,
			String password, Date dateEnregistrement, Entite entite) {
		super(nom, prenom, mail, adresse, telephone, mobile, password, dateEnregistrement, UserType.VISITEUR);
		// TODO Auto-generated constructor stub
		this.entite = entite;
	}

	public Entite getEntite() {
		return entite;
	}

	public void setEntite(Entite entite) {
		this.entite = entite;
	}

	
}
