package com.sigma.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.sigma.dto.AdministrateurEntiteDto;
import com.sigma.model.AdministrateurEntite;
import com.sigma.model.ApiResponse;
import com.sigma.model.Entite;
import com.sigma.model.Role;
import com.sigma.model.RoleType;
import com.sigma.repository.AdministrateurEntiteRepository;
import com.sigma.repository.EntiteRepository;
import com.sigma.repository.RoleRepository;
import com.sigma.service.impl.EmailServiceImpl;
import com.sigma.util.IterableToList;
import com.sigma.utilisateur.Utilisateur;
import com.sigma.utilisateur.UtilisateurRepository;


@Controller
@RequestMapping("/api/admins-entity")
public class AdmininistrateurEntiteController {

	@GetMapping
	@ResponseBody
	public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<AdministrateurEntite> users = IterableToList.toList(administrateurEntiteRepository.findAll());
			return objectMapper.writeValueAsString(users);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find Entity Administrators",
							ex)
					);
		}
	}
	
	@GetMapping("/with-any-entity")
	@ResponseBody
	public String listAny() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<AdministrateurEntite> users = administrateurEntiteRepository.findByEntiteIdIsNull();
			return objectMapper.writeValueAsString(users);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find Entity Administrators",
							ex)
					);
		}
	}


	@PostMapping
	@ResponseBody
	public String create(HttpServletRequest request, @RequestBody AdministrateurEntiteDto administrateur) throws com.fasterxml.jackson.core.JsonProcessingException {
		Utilisateur a;

		if (administrateur.getMail().length() == 0) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Mail cannot be empty")
					);
		}

		

		try {
			a = utilisateurRepository.findByMail(administrateur.getMail());
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find user " + administrateur.getMail(),
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
				AdministrateurEntite usr = new AdministrateurEntite(administrateur.getNom(), administrateur.getPrenom(),
						administrateur.getMail(), administrateur.getAdresse(), administrateur.getTelephone(),
						administrateur.getMobile(), bcryptEncoder.encode("nulll"),
						date, administrateur.getEntite());
				
				Role role = roleRepository.findByName(RoleType.ROLE_ADMINISTRATEUR_ENTITE.toString());
				
				usr.setRole(role);
				administrateurEntiteRepository.save(usr);
				String port = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
				String path = (request.getServerPort() == 80) ? "/p-sigma" : "";
				String choosePasswordUrl = String.format("%s://%s%s%s/new_password/%s",
						request.getScheme(),
						request.getServerName(),
						path,
						port,
						usr.getMail());
				emailService.sendInscriptionMessage(usr.getMail(), choosePasswordUrl, "administrateur d'entité");

			} catch (Exception ex) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Unable to create Entity Administrator",
								ex)
						);
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							"Entity Administrator successfully created\nReceived input:\n" + objectMapper.writeValueAsString(administrateur))
					);
		}
	}

	@RequestMapping("/delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			AdministrateurEntite user = administrateurEntiteRepository.findOne(id);
			Entite e = user.getEntite();
			if (e != null) {
				e.setAdministrateur(null);
				entiteRepository.save(e);
			}
			administrateurEntiteRepository.delete(user);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error when deleting the Entity Administrator",
							ex)
					);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Entity Administrator successfully deleted")
				);
	}
	
	/*
	 * Exemple : UrlConfig.API_URL/administrateur-entite/update?id=6
	 * Et mettre le body
	 */

	@RequestMapping("/update")
	@ResponseBody
	public String updateUser(@RequestBody AdministrateurEntiteDto administrateur) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {

			AdministrateurEntite user = administrateurEntiteRepository.findOne(administrateur.getId());
			user.setNom(administrateur.getNom());
	        user.setPrenom(administrateur.getPrenom());
	        user.setAdresse(administrateur.getAdresse());
	        user.setTelephone(administrateur.getTelephone());
			administrateurEntiteRepository.save(user);

		} catch (Exception ex) {
			return "Error updating the purchaser: " + ex.toString();
		}
		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Entity Administrator successfully updated")
				);
	}
	
	@GetMapping("/searchByName")
	  @ResponseBody
	  public String searchAdminEntity(@RequestParam String name) throws com.fasterxml.jackson.core.JsonProcessingException {
	    if (name == null) {
	      return objectMapper.writeValueAsString(
	        new ApiResponse(HttpStatus.EXPECTATION_FAILED,
	        "Le paramètre 'name' n'est pas fourni")
	      );
	    }

	    List<AdministrateurEntite> admins = administrateurEntiteRepository.findByNom(name);

	    return objectMapper.writeValueAsString(
	      new ApiResponse(HttpStatus.OK,
	      objectMapper.writeValueAsString(admins))
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
	private AdministrateurEntiteRepository administrateurEntiteRepository;
	
	@Autowired
	private EntiteRepository entiteRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
}
