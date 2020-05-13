package com.sigma.model;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.sigma.utilisateur.Utilisateur;

@Entity
@DiscriminatorValue("AC")
public class Acheteur extends Utilisateur {

	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// private long id;

	// @JsonIgnore
	@ManyToOne
	private Equipe equipe;

	@ManyToOne
	private Entite entite;
	
	@OneToMany(mappedBy="acheteur")
	private List<Segment> segments;

	

	public Acheteur(String nom, String prenom, String mail, Address adresse, String telephone, String mobile,
			Date dateEnregistrement, Equipe equipe,Entite entite, List<Segment> segments) {
		super(nom, prenom, mail, adresse, telephone, mobile, null, dateEnregistrement, UserType.PURCHASER);
		this.equipe = equipe;
		this.segments = segments;
		this.entite = entite;
	}

	public Acheteur() { }

	public Equipe getEquipe() {
		return this.equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public List<Segment> getSegments() {
		return this.segments;
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

	public void addSegment(Segment seg) {
		// TODO Auto-generated method stub
		segments.add(seg);
	}
	
	public void removeSegment(Segment seg) {
		// TODO Auto-generated method stub
		segments.remove(seg);
	}
	
}
