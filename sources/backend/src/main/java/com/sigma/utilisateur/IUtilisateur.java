package com.sigma.utilisateur;

import java.util.Date;

import com.sigma.model.Address;
import com.sigma.model.UserType;

public interface IUtilisateur {
  public String getNom();

  public void setNom(String nom);

  public String getPrenom();

  public void setPrenom(String prenom);

  public String getMail();

  public void setMail(String mail);

  public Address getAdresse();

  public void setAdresse(Address adresse);

  public String getPassword();

  public void setPassword(String password);

  public Date getDateEnregistrement();

  public void setDateEnregistrement(Date date);

  public UserType getUserType();

  public void setUserType(UserType type);
}
