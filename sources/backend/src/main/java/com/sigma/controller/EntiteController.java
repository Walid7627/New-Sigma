package com.sigma.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sigma.model.*;
import com.sigma.repository.ResponsableAchatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.sigma.config.JwtTokenUtil;
import com.sigma.dto.EntiteDto;
import com.sigma.dto.EquipeDto;
import com.sigma.repository.AdministrateurEntiteRepository;
import com.sigma.repository.EntiteRepository;
import com.sigma.repository.EquipeRepository;
import com.sigma.service.StorageService;
import com.sigma.service.impl.EntiteExcelServiceImpl;
import com.sigma.util.IterableToList;
import com.sigma.utilisateur.Utilisateur;
import com.sigma.utilisateur.UtilisateurRepository;


@Controller
@RequestMapping("/api/entities")
@CrossOrigin("*")
public class EntiteController {

	@GetMapping
	@ResponseBody
	public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Entite> entites = IterableToList.toList(entiteRepository.findAll());
			return objectMapper.writeValueAsString(entites);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find entities",
							ex)
					);
		}
	}

	@PostMapping
	@ResponseBody
	public String create(@RequestBody EntiteDto entite) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {

			Entite en = new Entite(entite.getNomSociete(), entite.getNumSiret(), entite.getLogo(), entite.getCodeAPE()
					, entite.getCodeCPV(), entite.getRaisonSociale(), entite.getTypeEntreprise(), entite.getMaisonMere(),
					entite.getSiteInstitutionnel(), entite.getDescription(), entite.getMail(), entite.getTelephone(),
					entite.getFax(), entite.getAdresse());
			en.setAdministrateur(entite.getAdministrateur());
			entiteRepository.save(en);
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							objectMapper.writeValueAsString(entite))
					);
		} catch (Exception ex) {
			System.err.println("Erreur : ");
			ex.printStackTrace();
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to create entity",
							ex)
					);
		}

	}

	@RequestMapping("/delete/{id}")
	@ResponseBody
	public String delete(@PathVariable Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Entite en = entiteRepository.findOne(id);
			entiteRepository.delete(en);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error when deleting the entity",
							ex)
					);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Entity successfully deleted")
				);
	}

	@RequestMapping("/update")
	@ResponseBody
	public String updateUser(@RequestBody EntiteDto entite) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			System.out.println("hey");
			Entite ent = entiteRepository.findById(entite.getId());
			System.out.println("hey");
			if (ent == null) {
				return "Entite inexistante";
			}
			ent.setNomSociete(entite.getNomSociete());
			ent.setNumSiret(entite.getNumSiret());
			ent.setLogo(entite.getLogo());
			ent.setCodeAPE(entite.getCodeAPE());
			ent.setCodeCPV(entite.getCodeCPV());
			ent.setRaisonSociale(entite.getRaisonSociale());
			ent.setTypeEntreprise(entite.getTypeEntreprise());
			ent.setMaisonMere((entite.getMaisonMere()));
			ent.setSiteInstitutionnel(entite.getSiteInstitutionnel());
			ent.setDescription(entite.getDescription());
			ent.setMail(entite.getMail());
			ent.setTelephone(entite.getTelephone());
			ent.setFax(entite.getFax());
			ent.setAdresse(entite.getAdresse());

			entiteRepository.save(ent);

		}
		catch (Exception ex) {
			return "Error updating the entity: " + ex.toString();
		}
		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Entity successfully updated")
				);
	}

	@PostMapping("/members")
	@ResponseBody
	public String listMembers(@RequestParam String mail) throws com.fasterxml.jackson.core.JsonProcessingException {
		AdministrateurEntite admin = administrateurEntiteRepository.findByMail(mail);
		if (admin == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"No privilege to list purchasers")
					);
		}
		try {
			List<Acheteur> acheteurs = admin.getEntite().getMembres();
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							objectMapper.writeValueAsString(acheteurs))
					);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find members",
							ex)
					);
		}
	}

	@PostMapping("/teams")
	@ResponseBody
	public String listTeams(@RequestParam String mail) throws com.fasterxml.jackson.core.JsonProcessingException {
		AdministrateurEntite admin = administrateurEntiteRepository.findByMail(mail);
		if (admin == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"No privilege to list teams")
					);
		}
		try {
			List<Equipe> equipes = admin.getEntite().getEquipes();
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							objectMapper.writeValueAsString(equipes))
					);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find teams",
							ex)
					);
		}
	}
	
	@RequestMapping("/admin/{id}/{ad_id}")
	@ResponseBody
	public String chooseAdmin(@PathVariable Long id, @PathVariable long ad_id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Entite en = entiteRepository.findById(id);
			AdministrateurEntite oldAd = en.getAdministrateur();
			if(oldAd != null) {
				oldAd.setEntite(null);
				administrateurEntiteRepository.save(oldAd);
			}
			AdministrateurEntite a = administrateurEntiteRepository.findOne(ad_id);

			en.setAdministrateur(a);
			a.setEntite(en);
			entiteRepository.save(en);
			administrateurEntiteRepository.save(a);

		} catch (Exception e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error",
							e)
					);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Administrateur ajouté avec succès")
				);
	}

	@PutMapping("/teams")
	@ResponseBody
	public String addTeam(@RequestParam Long id, @RequestBody EquipeDto equipe) throws com.fasterxml.jackson.core.JsonProcessingException {

		Entite en = entiteRepository.findById(id);

		Equipe eq = equipeRepository.findOneByEntiteIdAndLibelleIgnoreCase(id, equipe.getLibelle());

		if (eq != null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Team already exists in this entity")
			);
		} else {

			try {
				ResponsableAchat ra = responsableAchatRepository.findOne(equipe.getResponsable());
				Entite ent = entiteRepository.findById(equipe.getEntity());
				eq = new Equipe(equipe.getLibelle(), ra, ent, null);
				eq.setEntite(en);
				en.addEquipe(eq);
				equipeRepository.save(eq);
				entiteRepository.save(en);

			} catch (Exception ex) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Unable to create team",
								ex)
				);
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							"Team successfully created\nReceived input:\n" + objectMapper.writeValueAsString(equipe))
			);
		}
	}

	@GetMapping("/searchByName")
	@ResponseBody
	public String searchEntityByName(@RequestParam String name) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (name == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'name' n'est pas fourni")
					);
		}

		List<Entite> entities = entiteRepository.findByNomSociete(name);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(entities))
				);
	}

	@GetMapping("/searchByCpv")
	@ResponseBody
	public String searchEntityByCpv(@RequestParam String codecpv) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (codecpv == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'codecpv' n'est pas fourni")
					);
		}

		List<Entite> entities = entiteRepository.findByCodeCPV(codecpv);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(entities))
				);
	}

	@GetMapping("/searchByApe")
	@ResponseBody
	public String searchEntityByApe(@RequestParam String codeape) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (codeape == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'codeape' n'est pas fourni")
					);
		}

		List<Entite> entities = entiteRepository.findByCodeAPE(codeape);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(entities))
				);
	}
	@GetMapping("/searchById")
	@ResponseBody
	public String searchEntityByCpv(@RequestParam Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (id == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'id' n'est pas fourni")
					);
		}

		Entite entity = entiteRepository.findOne(id);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(entity))
				);
	}
	
	@GetMapping("/export")
	@ResponseBody
	public String getEntities(HttpServletRequest request, HttpServletResponse response)
			throws com.fasterxml.jackson.core.JsonProcessingException {
		System.out.println("\n\n heyyy\n\n");
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"No authentication token found")
					);
		}

		String authToken = header.substring(7);
		String userName = tokenUtil.getUsernameFromToken(authToken);

		Utilisateur user = utilisateurRepository.findByMail(userName);

		if (user == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"User found in token does not exist")
					);
		}

		if (user.getRole() == null || !user.getRole().getName().equals("ROLE_ADMINISTRATEUR_SIGMA")) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Insufficient privileges")
					);
		}

		List<Entite> entities = IterableToList.toList(entiteRepository.findAll());
		String path = "";
		try {
			path = excelService.createFile(entities);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to export entities",
							e)
					);
		}
		File file = storageService.get(path);
		if (file.exists()) {
			//get the mimetype
			String mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			response.setContentType(mimeType);

			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			//Here we have mentioned it to show as attachment
			//response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			try {
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

				FileCopyUtils.copy(inputStream, response.getOutputStream());

				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.OK,
								"OK")
						);

			} catch (IOException e) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.EXPECTATION_FAILED,
								"Error while copying stream",
								e)
						);
			}
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.EXPECTATION_FAILED,
						"File does not exist")
				);
	}


	@Autowired
	private EntiteRepository entiteRepository;

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private AdministrateurEntiteRepository administrateurEntiteRepository;
	
	@Autowired
	private ResponsableAchatRepository responsableAchatRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	EntiteExcelServiceImpl excelService;


	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	@Autowired
	JwtTokenUtil tokenUtil;

	@Autowired
	StorageService storageService;
}
