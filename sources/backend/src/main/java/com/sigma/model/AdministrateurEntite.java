package com.sigma.model;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.sigma.utilisateur.Utilisateur;

@Entity
@DiscriminatorValue("AE")
public class AdministrateurEntite extends Utilisateur {
	
	@OneToOne(mappedBy="administrateur")
	private Entite entite;
	
	
	public AdministrateurEntite() {
		// TODO Auto-generated constructor stub
	}
	public AdministrateurEntite(String nom, String prenom, String mail, Address adresse, String telephone, String mobile,
			String password, Date dateEnregistrement, Entite entite) {
		super(nom, prenom, mail, adresse, telephone, mobile, password, dateEnregistrement, UserType.ADMINENTITY);
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
