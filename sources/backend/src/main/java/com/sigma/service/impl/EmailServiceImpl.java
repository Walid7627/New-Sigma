package com.sigma.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.sigma.service.EmailService;

@Component
public class EmailServiceImpl implements EmailService {

  @Autowired
  public JavaMailSender emailSender;

  public void sendSimpleMessage(String to, String subject, String text) {

	  MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(text, "text/html; charset=utf-8");
			helper.setTo(to);
			helper.setSubject(subject);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	    
	    emailSender.send(mimeMessage);
    
    

  }
  
  public void sendInscriptionMessage(String mail, String url, String statut) {

	    String htmlMsg = "Vous avez été inscrit dans la plateforme SIGMA en tant que "+ statut +".\n"
	    		+ "Cliquez sur ce <a href=\"" + url + "\">lien</a> pour choisir votre mot de passe";
	    
	    MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(htmlMsg, "text/html; charset=utf-8");
			helper.setTo(mail);
			helper.setSubject("Inscription à la plateforme SIGMA");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	    
	    //emailSender.send(mimeMessage);
	    
	    

  }
}
