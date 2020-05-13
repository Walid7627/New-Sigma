package com.sigma.dto;

import com.sigma.model.Entite;

public class AdministrateurEntiteDto extends UtilisateurDto{
	private Entite entite;

	public Entite getEntite() {
		return entite;
	}

	public void setEntite(Entite entite) {
		this.entite = entite;
	}
}
