package com.sigma.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.sigma.model.*;
import com.sigma.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.dto.EquipeDto;
import com.sigma.util.IterableToList;

import javax.validation.constraints.Null;


@Controller
@RequestMapping("/api/team")
public class EquipeController {

	@GetMapping
	@ResponseBody
	public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Equipe> equipes = IterableToList.toList(equipeRepository.findAll());
			if (equipes.size() == 0) {
				System.out.print(objectMapper.writeValueAsString(equipes));
				return objectMapper.writeValueAsString(equipes);
			}

			String s = "[";
			for (Equipe eq : equipes) {
				s = s + objectMapper.writeValueAsString(eq) + ",";
			}
			s = s.substring(0, s.length() - 1);
			s = s + "]";

			//System.out.print(objectMapper.writeValueAsString(equipes));

			return s;
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find teams",
							ex)
			);
		}
	}

	/**
	 * POST /create  --> Create a new team and save it in the database.
	 */
	@PostMapping("/create/{mail}")
	@ResponseBody
	public String create(@PathVariable List<String> mail, @RequestBody EquipeDto equipe) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			String adminEmail = "";
			for (String s : mail) {
				adminEmail = adminEmail + "." + s;
			}
			adminEmail = adminEmail.substring(1);
			Acheteur ach = acheteurRepository.findOne(equipe.getResponsable());
			Entite en = administrateurEntiteRepository.findByMail(adminEmail).getEntite();
			List<Acheteur> liste = new ArrayList<Acheteur>();
			if (equipe.getMembres() != null) {
				for (Long ache : equipe.getMembres()) {
					Acheteur a = acheteurRepository.findOne(ache);
					if (a.getId() != ach.getId()) {
						a.setEntite(en);
						liste.add(a);
					}
				}

			}
			Equipe eq = new Equipe(equipe.getLibelle(), ach, en, liste);

			eq.getResponsable().setRole(roleRepository.findByName(RoleType.ROLE_RESPONSABLE_ACHAT.toString()));
			eq.getResponsable().setEntite(en);

			Long id = equipeRepository.save(eq).getId();
			addMember(id, eq.getResponsable().getId());
			eq.getResponsable().setEquipe(equipeRepository.findById(id));
			if (equipe.getMembres() != null) {
				for (Long acheteur : equipe.getMembres()) {
					addMember(id, acheteur);
				}
			}


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

	/**
	 * GET /delete  --> Delete the user having the passed id.
	 */
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findOne(id);

			eq.getResponsable().setRole(roleRepository.findByName(RoleType.ROLE_ACHETEUR.toString()));

			for (int k = 0; k < eq.getMembres().size(); ++k) {
				removeMember(id, eq.getMembres().get(k).getId());
			}

			removeMember(id, eq.getResponsable().getId());

			equipeRepository.delete(eq);

		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error when deleting the team",
							ex)
					);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Team successfully deleted")
				);
	}


	/**
	 * GET /update  --> Update the email and the name for the user in the
	 * database having the passed id.
	 */
	@PostMapping("/update/{id}")
	@ResponseBody
	public String update(@PathVariable Long id, @RequestBody EquipeDto equipe) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			System.out.print("\n\n11111111111 equipe_id:"+id+" new_resp"+equipe.getResponsable()+"  new_entite"+equipe.getEntity());
			Equipe eq = equipeRepository.findById(id);
			if (equipe.getLibelle() != null) {
				eq.setLibelle(equipe.getLibelle());
			}

			if (equipe.getResponsable() != eq.getResponsable().getId() && equipe.getResponsable() != null) {
				Acheteur ra = acheteurRepository.findOne(equipe.getResponsable());
				ra.setRole(roleRepository.findByName(RoleType.ROLE_RESPONSABLE_ACHAT.toString()));
				ra.setEntite(eq.getEntite());
				ra.setEquipe(eq);
				acheteurRepository.save(ra);

				Acheteur resp = acheteurRepository.findOne(eq.getResponsable().getId());
				resp.setRole(roleRepository.findByName(RoleType.ROLE_ACHETEUR.toString()));
				resp.setEquipe(null);
				//resp.setEntite(null);
				acheteurRepository.save(resp);

				eq.setResponsable(ra);
			}

			equipeRepository.save(eq);

		}
		catch (Exception ex) {
			return "Error updating the team: " + ex.toString();
		}
		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Team successfully updated")
				);
	}


	@RequestMapping("/acheteurs/add")
	@ResponseBody
	public String addMember(@RequestParam Long equipe, @RequestParam Long acheteur) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findById(equipe);
			Acheteur a = acheteurRepository.findOne(acheteur);

				if (a.getEquipe() == eq) {
					return objectMapper.writeValueAsString(
							new ApiResponse(HttpStatus.BAD_REQUEST,
									"Le membre fait déjà partie de cette équipe")
					);
				} else {
					eq.addMembre(a);
					a.setEquipe(eq);
					a.setEntite(eq.getEntite());
					acheteurRepository.save(a);
					equipeRepository.save(eq);
				}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							"Membre ajouté avec succès")
			);
		} catch (Exception e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error",
							e)
					);
		}


	}

	@GetMapping("/members")
	@ResponseBody
	public String listMembers(@RequestParam Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Acheteur> acheteurs = acheteurRepository.findByEquipeId(id);
			return objectMapper.writeValueAsString(acheteurs);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find members",
							ex)
					);
		}
	}

	@DeleteMapping("/members")
	@ResponseBody
	public String removeMember(@RequestParam Long id, @RequestParam long acheteur_id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findById(id);
			Acheteur a = acheteurRepository.findOne(acheteur_id);
			a.setEquipe(null);
			//a.setEntite(null);
			acheteurRepository.save(a);
            equipeRepository.save(eq);

		} catch (Exception e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error",
							e)
					);
		}
		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Membre retiré avec succès")
				);
	}


	@RequestMapping("/acheteurs/supprimer")
	@ResponseBody
	public String supprimerAcheteur(@RequestParam Long equipe, @RequestParam Long acheteur) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findById(equipe);
			Acheteur a = acheteurRepository.findOne(acheteur);
			if (a.getEquipe() == null) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Cet acheteur ne correspond à aucune équipe")
				);
			}
			a.setEquipe(null);
			//a.setEntite(null);

			acheteurRepository.save(a);
			equipeRepository.save(eq);

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							"Acheteur retiré avec succès")
			);
		} catch (Exception e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error",
							e)
			);
		}
	}



	@RequestMapping("/responsable")
	@ResponseBody
	public String chooseResponsable(@RequestParam Long id, @RequestParam long acheteur_id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Equipe eq = equipeRepository.findById(id);
			Acheteur oldResp = eq.getResponsable();
			if(oldResp != null) {
				Acheteur usr = new Acheteur(oldResp.getNom(), oldResp.getPrenom(),
						oldResp.getMail(), oldResp.getAdresse(), oldResp.getTelephone(), oldResp.getMobile(),
						oldResp.getDateEnregistrement(), oldResp.getEquipe(), oldResp.getEntite(),oldResp.getSegments());
				usr.setPassword(oldResp.getPassword());
				acheteurRepository.delete(oldResp);
				usr.setRole(roleRepository.findByName(RoleType.ROLE_ACHETEUR.toString()));
				acheteurRepository.save(usr);
			}
			Acheteur responsableAchat = acheteurRepository.findOne(acheteur_id);
			responsableAchat.setRole(roleRepository.findByName(RoleType.ROLE_RESPONSABLE_ACHAT.toString()));
			eq.setResponsable(responsableAchat);

			acheteurRepository.save(responsableAchat);
			equipeRepository.save(eq);


		} catch (Exception e) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Error",
							e)
					);
		}

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						"Responsable d'achat ajouté à l'equipe avec succès")
				);
	}

	@GetMapping("/searchByName")
	@ResponseBody
	public String searchTeamByName(@RequestParam String name) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (name == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'name' n'est pas fourni")
					);
		}

		List<Equipe> teams = equipeRepository.findByLibelle(name);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(teams))
				);
	}

	@PostMapping("/searchById")
	@ResponseBody
	public String searchTeamByID(@RequestParam Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (id == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'id' n'est pas fourni")
					);
		}

		Equipe team = equipeRepository.findOne(id);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(team))
				);
	}

	// Private fields

	@Autowired
	private EquipeRepository equipeRepository;

	@Autowired
	private ResponsableAchatRepository responsableRepository;

	@Autowired
	private EntiteRepository entiteRepository;

	@Autowired
	private AcheteurRepository acheteurRepository;

	@Autowired
	private AdministrateurEntiteRepository administrateurEntiteRepository;

	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	@Autowired
	RoleRepository roleRepository;
}
