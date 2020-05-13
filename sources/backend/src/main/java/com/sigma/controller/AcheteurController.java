package com.sigma.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.dto.AcheteurDto;
import com.sigma.model.Acheteur;
import com.sigma.model.AdministrateurEntite;
import com.sigma.model.ApiResponse;
import com.sigma.model.RoleType;
import com.sigma.repository.AcheteurRepository;
import com.sigma.repository.AdministrateurEntiteRepository;
import com.sigma.repository.RoleRepository;
import com.sigma.service.impl.EmailServiceImpl;
import com.sigma.util.IterableToList;
import com.sigma.utilisateur.Utilisateur;
import com.sigma.utilisateur.UtilisateurRepository;


@Controller
@RequestMapping("/api/purchasers")
public class AcheteurController {

	@Autowired
	private AcheteurRepository acheteurRepository;
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	EmailServiceImpl emailService;
	
	@Autowired
	private AdministrateurEntiteRepository administrateurEntiteRepository;

	// Private fields

	@GetMapping
	@ResponseBody
	public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Acheteur> users = IterableToList.toList(acheteurRepository.findAll());
			return objectMapper.writeValueAsString(users);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find purchasers",
							ex)
					);
		}
	}
	
	@GetMapping("searchByTeam/{id}")
	@ResponseBody
	public String listInTeam(@PathVariable Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Acheteur> users = acheteurRepository.findByEquipeId(id);
			return objectMapper.writeValueAsString(users);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find purchasers",
							ex)
					);
		}
	}
	
	@GetMapping("/with-no-team")
	@ResponseBody
	public String list_without_team() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Acheteur> users = IterableToList.toList(acheteurRepository.findAll());
			List<Acheteur> users_test = new ArrayList<Acheteur>();
			for (Acheteur user : users) {
				if (user.getEquipe() == null) users_test.add(user);
			}
			return objectMapper.writeValueAsString(users_test);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find purchasers",
							ex)
			);
		}
	}

	@GetMapping("searchByEntite/{id}")
	@ResponseBody
	public String listInEntite(@PathVariable Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Acheteur> users = acheteurRepository.findByEntiteId(id);
			return objectMapper.writeValueAsString(users);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find purchasers",
							ex)
					);
		}
	}


	/**
	 * POST /create  --> Create a new user and save it in the database.
	 */
	@PostMapping
	@ResponseBody
	public String create(HttpServletRequest request, @RequestBody AcheteurDto acheteur) throws com.fasterxml.jackson.core.JsonProcessingException {
		
		AdministrateurEntite admin = administrateurEntiteRepository.findByMail(acheteur.getEntite().getAdministrateur().getMail());
		if (admin == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"No privilege to add purchasers")
					);
		}
		
		
		Utilisateur a;

		if (acheteur.getMail().length() == 0) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Mail cannot be empty")
					);
		}

		try {
			a = utilisateurRepository.findByMail(acheteur.getMail());
		} catch (Exception ex) {
			
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find purchaser " + acheteur.getMail(),
							ex)
					);
		}

		if (a != null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"User already exists")
					);
		} else {
			String userId = "";

			Date date = new Date();
			try {
				Acheteur usr = new Acheteur(acheteur.getNom(), acheteur.getPrenom(),
						acheteur.getMail(), acheteur.getAdresse(), acheteur.getTelephone(), acheteur.getMobile(),
						date, acheteur.getEquipe(), admin.getEntite(), acheteur.getSegments());

				usr.setRole(roleRepository.findByName(RoleType.ROLE_ACHETEUR.toString()));
				acheteurRepository.save(usr);
				userId = String.valueOf(usr.getId());
				String port = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
				String path = (request.getServerPort() == 80) ? "/p-sigma" : "";
				String choosePasswordUrl = String.format("%s://%s%s%s/new_password/%s",
						request.getScheme(),
						request.getServerName(),
						path,
						port,
						usr.getMail());
				emailService.sendInscriptionMessage(usr.getMail(), choosePasswordUrl, "acheteur");
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
							"User successfully created (id: " + userId + ")\nReceived input:\n" + objectMapper.writeValueAsString(acheteur))
					);
		}
	}

	/**
	 * GET /delete  --> Delete the user having the passed id.
	 */
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Acheteur user = acheteurRepository.findOne(id);
			acheteurRepository.delete(user);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error when deleting the user",
							ex)
					);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"User successfully deleted")
				);
	}

	/**
	 * PUT /update  --> Update the email and the name for the user in the
	 * database having the passed id.
	 */
	@PutMapping
	@ResponseBody
	public String updateUser(@RequestParam Long id, @RequestBody AcheteurDto acheteur) throws com.fasterxml.jackson.core.JsonProcessingException {
		Acheteur user;
		try {
			user = acheteurRepository.findOne(acheteur.getId());

			user.setNom(acheteur.getNom());
			user.setPrenom(acheteur.getPrenom());
			user.setAdresse(acheteur.getAdresse());
			user.setTelephone(acheteur.getTelephone());
			user.setMobile(acheteur.getMobile());

			acheteurRepository.save(user);
		}
		catch (Exception ex) {
			return "Error updating the user: " + ex.toString();
		}
		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(user))
				);
	}

	@GetMapping("/searchByName")
	@ResponseBody
	public String searchPurchasersByName(@RequestParam String name) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (name == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'name' n'est pas fourni")
					);
		}

		List<Acheteur> achs = acheteurRepository.findByNom(name);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(achs))
				);
	}

	@GetMapping("/searchById")
	@ResponseBody
	public String searchPurchaserByID(@RequestParam Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (id == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'id' n'est pas fourni")
					);
		}

		Acheteur ach = acheteurRepository.findOne(id);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(ach))
				);
	}
}
