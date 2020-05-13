package com.sigma.model;

import java.util.Date;
import java.util.List;

// import com.sigma.utilisateur.Utilisateur;

import javax.persistence.*;

@Entity
public class ResponsableAchat extends Acheteur {
  public ResponsableAchat(String nom, String prenom, String mail, Address adresse, String telephone, String mobile,
                   Date dateEnregistrement, Equipe equipe, Entite entite, List<Segment> segments) {
    super(nom, prenom, mail, adresse, telephone, mobile, dateEnregistrement, equipe, entite,segments);
    setUserType(UserType.RESPONSABLE);

  }

  public ResponsableAchat() { }
}
