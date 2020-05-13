package com.sigma.dto;

import java.util.List;

import com.sigma.model.Entite;
import com.sigma.model.Equipe;
import com.sigma.model.Segment;

public class AcheteurDto extends UtilisateurDto {
  private Equipe equipe;
  private Entite entite;
  private List<Segment> segments;

  public Equipe getEquipe() {
    return this.equipe;
  }

  public void setEquipe(Equipe equipe) {
    this.equipe = equipe;
  }

public List<Segment> getSegments() {
	return segments;
}

public void setSegments(List<Segment> segments) {
	this.segments = segments;
}

public Entite getEntite() {
	return entite;
}

public void setEntite(Entite entite) {
	this.entite = entite;
}

  
}
