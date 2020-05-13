package com.sigma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.dto.AdministrateurSigmaDto;
import com.sigma.model.AdministrateurSigma;
import com.sigma.model.ApiResponse;
import com.sigma.model.RoleType;
import com.sigma.repository.AdministrateurSigmaRepository;
import com.sigma.repository.RoleRepository;
import com.sigma.utilisateur.Utilisateur;
import com.sigma.utilisateur.UtilisateurRepository;

@Controller
@RequestMapping("/api/admins-sigma")
public class AdministrateurSigmaController {
	@PostMapping
	@ResponseBody
	public String create(@RequestBody AdministrateurSigmaDto administrateur) throws com.fasterxml.jackson.core.JsonProcessingException {
		Utilisateur f;

		if (administrateur.getMail().length() == 0) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Mail cannot be empty")
					);
		}

		if (administrateur.getPassword().length() == 0) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Password cannot be empty")
					);
		}

		try {
			f = utilisateurRepository.findByMail(administrateur.getMail());
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find user " + administrateur.getMail(),
							ex)
					);
		}

		if (f != null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"User already exists")
					);
		} else {
			String response = "";
			try {
				AdministrateurSigma usr = new AdministrateurSigma(administrateur.getMail(), bcryptEncoder.encode(administrateur.getPassword()));

				usr.setRole(roleRepository.findByName(RoleType.ROLE_ADMINISTRATEUR_SIGMA.toString()));

				admintrateurSigmaRepository.save(usr);
				response = String.valueOf(usr.getMail());

			} catch (Exception ex) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Unable to create user",
								ex)
						);
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							objectMapper.writeValueAsString(response))
					);
		}
	}


	@Autowired
	private AdministrateurSigmaRepository admintrateurSigmaRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;


	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

}
