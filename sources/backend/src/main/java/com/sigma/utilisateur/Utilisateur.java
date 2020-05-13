package com.sigma.utilisateur;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sigma.model.Address;
import com.sigma.model.Role;
import com.sigma.model.UserType;

@Entity
@Inheritance
@DiscriminatorColumn(name = "PROJ_TYPE")
@Table(name = "utilisateur")
public class Utilisateur implements IUtilisateur {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String nom;
  private String prenom;
  private String mail;
  @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval=true)
  private Address adresse;
  private String password;
  private Date dateEnregistrement;
  private UserType userType;
  private String telephone;
  private String mobile;

  @OneToOne
  private Role role;

  public Utilisateur() {
  }

  public Utilisateur(Long id) {
    this.id = id;
  }

  public Utilisateur(String nom, String prenom, String mail, Address adresse, String telephone, String mobile,
  String password, Date dateEnregistrement, UserType userType) {
    this.nom = nom;
    this.prenom = prenom;
    this.mail = mail;
    this.adresse = adresse;
    this.password = password;
    this.dateEnregistrement = dateEnregistrement;
    this.userType = userType;
    this.telephone = telephone;
    this.mobile = mobile;
  }

  public Utilisateur(String nom, String prenom, String mail, String password) {
    this.nom = nom;
    this.prenom = prenom;
    this.mail = mail;
    this.password = password;
  }

  public Long getId() {
    return this.id;
  }

  public String getNom() {
    return this.nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPrenom() {
    return this.prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public String getMail() {
    return this.mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  public Address getAdresse() {
    return this.adresse;
  }

  public void setAdresse(Address adresse) {
    this.adresse = adresse;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getDateEnregistrement() {
    return this.dateEnregistrement;
  }

  public void setDateEnregistrement(Date dateEnregistrement) {
    this.dateEnregistrement = dateEnregistrement;
  }

  public UserType getUserType() {
    return this.userType;
  }

  public void setUserType(UserType type) {
    this.userType = type;
  }

  public String getTelephone() {
    return this.telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getMobile() {
    return this.mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public Role getRole() {
    return this.role;
  }

  public void setRole(Role r) {
    this.role = r;
  }


}
