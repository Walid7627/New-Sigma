package com.sigma.controller;

import com.sigma.model.ApiResponse;
import com.sigma.model.Role;
import com.sigma.repository.RoleRepository;
import com.sigma.util.IterableToList;
import com.sigma.utilisateur.Utilisateur;
import com.sigma.utilisateur.UtilisateurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;



@Controller
@RequestMapping("/api/role")
public class RoleController {

  @GetMapping
  @ResponseBody
  public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
    try {
      List<Role> roles = IterableToList.toList(roleRepository.findAll());
      return objectMapper.writeValueAsString(roles);
    } catch (Exception ex) {
      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.BAD_REQUEST,
                        "Unable to find users",
                        ex)
      );
    }
  }

  @PutMapping
  @ResponseBody
  public String setRole(@RequestParam String userMail, @RequestParam String roleName)
    throws com.fasterxml.jackson.core.JsonProcessingException {
    try {
      Utilisateur user = utilisateurRepository.findByMail(userMail);

      if (user == null) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
                          "Unable to find user")
        );
      }

      Role r = roleRepository.findByName(roleName);

      if (r == null) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
                          "Unable to find specified role")
        );
      }

      user.setRole(r);
      utilisateurRepository.save(user);

      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.BAD_REQUEST,
                        "Role successfully added to user")
      );
    } catch (Exception ex) {
      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.BAD_REQUEST,
                        "Error while adding role to user",
                        ex)
      );
    }
  }

  @PutMapping("/removeAll")
  @ResponseBody
  public String removeRole(@RequestParam String userMail)
    throws com.fasterxml.jackson.core.JsonProcessingException {
    try {
      Utilisateur user = utilisateurRepository.findByMail(userMail);

      if (user == null) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
                          "Unable to find user")
        );
      }

      user.setRole(null);
      utilisateurRepository.save(user);

      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.BAD_REQUEST,
                        "Role successfully removed from user")
      );
    } catch (Exception ex) {
      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.BAD_REQUEST,
                        "Error while adding role to user",
                        ex)
      );
    }
  }

  @Autowired
  private UtilisateurRepository utilisateurRepository;

  @Autowired
  private RoleRepository roleRepository;


  @Autowired
  private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

}
