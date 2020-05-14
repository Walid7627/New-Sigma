package com.sigma.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sigma.utilisateur.Utilisateur;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("F")
public class Fournisseur extends Utilisateur {

  // @Id
  // @GeneratedValue(strategy = GenerationType.AUTO)
  // private long id;

  private String numSiret;
  private String logo;
  private String nomSociete;
  private String codeAPE;
  @ElementCollection
  @CollectionTable(name = "fournisseur_cpv")
  private List<String> codeCPV;
  private String raisonSociale;
  private String siteInstitutionnel;
  private String description;
  private FournisseurType typeEntreprise;
  private Long maisonMere;
  private String fax;
  // private List<Document> documents;
  // private Qualification qualifications;
  @OneToOne(cascade = {CascadeType.ALL})
  private Qualification qualification;

  @OneToMany(
  cascade = CascadeType.ALL,
  orphanRemoval = true
  )
  private List<Metrique> evaluations;

  @OneToMany(
  cascade = CascadeType.ALL,
  orphanRemoval = true
  )
  private List<Document> documents;
  private Boolean enabled;

  @OneToMany(mappedBy="fournisseur",cascade = CascadeType.ALL,
		  orphanRemoval = true)
  @JsonManagedReference
  private List<Contact> contacts;

  public Fournisseur() {
  }

  public Fournisseur(String nom, String prenom, String mail, Address adresse, String telephone, String mobile,
  String fax, String password, Date dateEnregistrement, String siret,
  String logo, String nomSociete, FournisseurType typeEntreprise, Long maisonMere, String codeAPE,
  List<String> codeCPV, String raisonSociale, String siteInstitutionnel,
  String description, List<Metrique> evaluations, List<Document> documents, Qualification q) {
    super(nom, prenom, mail, adresse, telephone, mobile, password, dateEnregistrement, UserType.PROVIDER);
    this.numSiret = siret;
    this.logo = logo;
    this.codeAPE = codeAPE;
    this.codeCPV = codeCPV;
    this.raisonSociale = raisonSociale;
    this.siteInstitutionnel = siteInstitutionnel;
    this.description = description;
    this.evaluations = evaluations;
    this.typeEntreprise = typeEntreprise;
    this.maisonMere = maisonMere;
    this.nomSociete = nomSociete;
    this.fax = fax;
    this.documents = documents;
    this.enabled = false;
    this.qualification = q;
  }

  public String getNumSiret() {
    return this.numSiret;
  }

  public void setNumSiret(String siret) {
    this.numSiret = siret;
  }

  public String getLogo() {
    return this.logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
  }

  public String getNomSociete() {
    return this.nomSociete;
  }

  public void setNomSociete(String nom) {
    this.nomSociete = nom;
  }

  public String getCodeAPE() {
    return this.codeAPE;
  }

  public void setCodeAPE(String code) {
    this.codeAPE = code;
  }

  public List<String> getCodeCPV() {
    return this.codeCPV;
  }

  public void setCodeCPV(List<String> code) {
    this.codeCPV = code;
  }
  public boolean containsCpv(String cpv) {
	  return this.codeCPV.contains(cpv);
	  
  }

  public String getRaisonSociale() {
    return this.raisonSociale;
  }

  public void setRaisonSociale(String raisonSociale) {
    this.raisonSociale = raisonSociale;
  }

  public String getSiteInstitutionnel() {
    return this.siteInstitutionnel;
  }

  public void setSiteInstitutionnel(String siteInstitutionnel) {
    this.siteInstitutionnel = siteInstitutionnel;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Metrique> getEvaluations() {
    return this.evaluations;
  }

  public void setEvaluation(List<Metrique> evaluations) {
    this.evaluations = evaluations;
  }

  public void addEvaluation(Metrique evaluation) {
    this.evaluations.add(evaluation);
  }

  public void removeEvaluation(Metrique evaluation) {
    this.evaluations.remove(evaluation);
  }

  public FournisseurType getTypeEntreprise() {
    return this.typeEntreprise;
  }

  public void setTypeEntreprise(FournisseurType type) {
    this.typeEntreprise = type;
  }

  public Long getMaisonMere() {
    return this.maisonMere;
  }

  public void setMaisonMere(Long maisonMere) {
    this.maisonMere = maisonMere;
  }

  public String getFax() {
    return this.fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public Qualification getQualification() {
    return this.qualification;
  }

  public void setQualification(Qualification q) {
    qualification = q;
  }


  public Document getDocument(String name) {
    // Optional<Document> result = this.documents
    //       .stream()
    //       .filter(a -> a.getName() == name)
    //       .findFirst();
    //
    // if (result.isPresent()) {
    //   return result.get();
    // } else {
    //   return null;
    // }

    for (Document d : this.documents) {
      if (d.getName().equals(name)) {
        return d;
      }
    }

    return null;
  }

  public List<Document> getDocuments() {
    return this.documents;
  }

  public void addDocument(Document d) {
    this.documents.add(d);
  }

  public void updateDocument(Document d) {
    if (this.documents.contains(d)) {
      this.removeDocument(d);
    }

    this.addDocument(d);
  }

  public void removeDocument(Document d) {
    this.documents.remove(d);
  }

  public Boolean isEnabled() {
    return this.enabled;
  }

  public void setEnabled() {
    this.enabled = true;
  }


  public List<Contact> getContacts() {
    return this.contacts;
  }

  public void setContacts(List<Contact> contacts) {
    this.contacts = contacts;
  }

  public void addContact(Contact contact) {
    this.contacts.add(contact);
  }

  public void removeContact(Contact contact) {
    this.contacts.remove(contact);
  }
}
