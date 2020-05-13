package com.sigma.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.sigma.utilisateur.Utilisateur;
@Entity
@DiscriminatorValue("A")
public class AdministrateurSigma extends Utilisateur{
	public AdministrateurSigma(String mail, String password) {
		setMail(mail);
		setPassword(password);
		setUserType(UserType.ADMINSIGMA);
	}
	
	public AdministrateurSigma() {
		// TODO Auto-generated constructor stub
	}
}
