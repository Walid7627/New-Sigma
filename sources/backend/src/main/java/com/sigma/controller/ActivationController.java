package com.sigma.controller;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.model.ApiResponse;
import com.sigma.model.Fournisseur;
import com.sigma.model.VerificationToken;
import com.sigma.repository.FournisseurRepository;
import com.sigma.repository.VerificationTokenRepository;

@Controller
@RequestMapping("/api/activate/{token}")
public class ActivationController {

  @GetMapping
  @ResponseBody
  public String activate(@PathVariable String token) throws com.fasterxml.jackson.core.JsonProcessingException {

    if (token == null) {
      return objectMapper.writeValueAsString(
      new ApiResponse(HttpStatus.EXPECTATION_FAILED,
      "Activation token is missing")
      );
    }

    VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

    if (verificationToken == null) {
      return objectMapper.writeValueAsString(
      new ApiResponse(HttpStatus.EXPECTATION_FAILED,
      "Activation token not found in database")
      );
    }

    Calendar cal = Calendar.getInstance();
    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
      return objectMapper.writeValueAsString(
      new ApiResponse(HttpStatus.EXPECTATION_FAILED,
      "Le lien d'activation a expiré.")
      );
    }

    Fournisseur usr = verificationToken.getUser();
    usr.setEnabled();
    fournisseurRepository.save(usr);

    return objectMapper.writeValueAsString(
    new ApiResponse(HttpStatus.OK,
    "User successfully activated")
    );
  }

  @Autowired
  private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

  @Autowired
  private VerificationTokenRepository verificationTokenRepository;

  @Autowired
  private FournisseurRepository fournisseurRepository;
}
