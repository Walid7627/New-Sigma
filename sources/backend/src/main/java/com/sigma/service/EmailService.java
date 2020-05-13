package com.sigma.service;

public interface EmailService {

  // Send simple Email
  public void sendSimpleMessage(String to, String subject, String text);
  public void sendInscriptionMessage(String mail, String url, String statut);
}
