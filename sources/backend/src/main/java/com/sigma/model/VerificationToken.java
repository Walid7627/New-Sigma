package com.sigma.model;

import javax.persistence.*;
import com.sigma.utilisateur.Utilisateur;
import java.util.Date;
import java.util.Calendar;
import java.util.UUID;
import java.sql.Timestamp;


@Entity
public class VerificationToken {
  private static final int EXPIRATION = 60 * 24;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String token;

  @OneToOne(targetEntity = Utilisateur.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private Fournisseur user;

  private Date expiryDate;

  private Date calculateExpiryDate(int expiryTimeInMinutes) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Timestamp(cal.getTime().getTime()));
    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
    return new Date(cal.getTime().getTime());
  }

  public VerificationToken() { }

  public VerificationToken(Fournisseur user) {
    this.user = user;
    this.token = UUID.randomUUID().toString();
    this.expiryDate = this.calculateExpiryDate(EXPIRATION);
  }

  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Fournisseur getUser() {
    return this.user;
  }

  public void setUser(Fournisseur user) {
    this.user = user;
  }

  public Date getExpiryDate() {
    return this.expiryDate;
  }

  public void setExpiryDate(Date date) {
    this.expiryDate = date;
  }

}
