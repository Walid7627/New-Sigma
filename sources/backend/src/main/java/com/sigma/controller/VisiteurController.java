

package com.sigma.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.dto.VisiteurDto;
import com.sigma.model.ApiResponse;
import com.sigma.model.Entite;
import com.sigma.model.Role;
import com.sigma.model.RoleType;
import com.sigma.model.Visiteur;
import com.sigma.repository.VisiteurRepository;
import com.sigma.repository.EntiteRepository;
import com.sigma.repository.RoleRepository;
import com.sigma.service.impl.EmailServiceImpl;
import com.sigma.util.IterableToList;
import com.sigma.utilisateur.Utilisateur;
import com.sigma.utilisateur.UtilisateurRepository;


@Controller
@RequestMapping("/api/visiteur")
public class VisiteurController {

	@GetMapping
	@ResponseBody
	public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Visiteur> users = IterableToList.toList(visiteurRepository.findAll());
			return objectMapper.writeValueAsString(users);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find Visiteur",
							ex)
					);
		}
	}
	
	@GetMapping("/with-any-entity")
	@ResponseBody
	public String listAny() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Visiteur> users = visiteurRepository.findByEntiteIdIsNull();
			return objectMapper.writeValueAsString(users);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find Entity Visiteur",
							ex)
					);
		}
	}


	@PostMapping
	@ResponseBody
	public String create(HttpServletRequest request, @RequestBody VisiteurDto visiteur) throws com.fasterxml.jackson.core.JsonProcessingException {
		Utilisateur a;

		if (visiteur.getMail().length() == 0) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Mail cannot be empty")
					);
		}

		

		try {
			a = utilisateurRepository.findByMail(visiteur.getMail());
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find user " + visiteur.getMail(),
							ex)
					);
		}

		if (a != null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"L'adresse mail correspond déjà à un utiliseur")
					);
		} else {


			Date date = new Date();
			
			try {
				Visiteur usr = new 	Visiteur(visiteur.getNom(), visiteur.getPrenom(),
						visiteur.getMail(), visiteur.getAdresse(), visiteur.getTelephone(),
						visiteur.getMobile(), bcryptEncoder.encode("nulll"),
						date, visiteur.getEntite());
				
				Role role = roleRepository.findByName(RoleType.ROLE_VISITEUR.toString());
				
				usr.setRole(role);
				visiteurRepository.save(usr);
				String port = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
				String path = (request.getServerPort() == 80) ? "/p-sigma" : "";
				String choosePasswordUrl = String.format("%s://%s%s%s/new_password/%s",
						request.getScheme(),
						request.getServerName(),
						path,
						port,
						usr.getMail());
				emailService.sendInscriptionMessage(usr.getMail(), choosePasswordUrl, "visiteur");
			} catch (Exception ex) {
				ex.printStackTrace();
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Unable to create user",
								ex)
						);
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							"Visiteur successfully created\nReceived input:\n" + objectMapper.writeValueAsString(visiteur))
					);
		}
	}

	@RequestMapping("/delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Visiteur user = visiteurRepository.findOne(id);
			/*Entite e = user.getEntite();
			if (e != null) {
				e.setAdministrateur(null);
				entiteRepository.save(e);
			}*/
			visiteurRepository.delete(user);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error when deleting the Visiteur",
							ex)
					);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Visiteur successfully deleted")
				);
	}
	
	/*
	 * Exemple : UrlConfig.API_URL/visiteur/update?id=6
	 * Et mettre le body
	 */

	@RequestMapping("/update")
	@ResponseBody
	public String updateUser(@RequestBody VisiteurDto visiteur) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {

			Visiteur user = visiteurRepository.findOne(visiteur.getId());
			user.setNom(visiteur.getNom());
	        user.setPrenom(visiteur.getPrenom());
	        user.setAdresse(visiteur.getAdresse());
	        user.setTelephone(visiteur.getTelephone());
			visiteurRepository.save(user);

		} catch (Exception ex) {
			return "Error updating the purchaser: " + ex.toString();
		}
		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Visiteur successfully updated")
				);
	}
	
	@GetMapping("/searchByName")
	  @ResponseBody
	  public String searchVisiteur(@RequestParam String name) throws com.fasterxml.jackson.core.JsonProcessingException {
	    if (name == null) {
	      return objectMapper.writeValueAsString(
	        new ApiResponse(HttpStatus.EXPECTATION_FAILED,
	        "Le paramètre 'name' n'est pas fourni")
	      );
	    }

	    List<Visiteur> visiteurs = visiteurRepository.findByNom(name);

	    return objectMapper.writeValueAsString(
	      new ApiResponse(HttpStatus.OK,
	      objectMapper.writeValueAsString(visiteurs))
	    );
	  }


	@Autowired
	EmailServiceImpl emailService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private VisiteurRepository visiteurRepository;
	
	@Autowired
	private EntiteRepository entiteRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
}
