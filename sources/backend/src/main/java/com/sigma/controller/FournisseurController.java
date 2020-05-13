package com.sigma.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.config.JwtTokenUtil;
import com.sigma.dto.ContactDto;
import com.sigma.dto.FournisseurDto;
import com.sigma.model.ApiResponse;
import com.sigma.model.Contact;
import com.sigma.model.Document;
import com.sigma.model.Fournisseur;
import com.sigma.model.FournisseurType;
import com.sigma.model.RoleType;
import com.sigma.model.VerificationToken;
import com.sigma.repository.ContactRepository;
import com.sigma.repository.DocumentRepository;
import com.sigma.repository.FournisseurRepository;
import com.sigma.repository.RoleRepository;
import com.sigma.repository.VerificationTokenRepository;
import com.sigma.service.StorageService;
import com.sigma.service.impl.EmailServiceImpl;
import com.sigma.service.impl.FournisseurExcelServiceImpl;
import com.sigma.util.IterableToList;
import com.sigma.utilisateur.Utilisateur;
import com.sigma.utilisateur.UtilisateurRepository;

@Controller
@RequestMapping("/api/providers")
public class FournisseurController {

	@GetMapping
	@ResponseBody
	public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Fournisseur> users = IterableToList.toList(fournisseurRepository.findAll());
			return objectMapper.writeValueAsString(users);
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find users",
							ex)
					);
		}
	}
	
	@GetMapping("/last")
	@ResponseBody
	public String listOrder() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Fournisseur> users =fournisseurRepository.findAllByOrderByDateEnregistrementDesc(); 
			int n = Math.min(users.size(), 10);
			System.out.println("\n\n n = " + n);
			return objectMapper.writeValueAsString(users.subList(0, n));
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find users",
							ex)
					);
		}
	}


	/**
	 * POST /create  --> Create a new user and save it in the database.
	 */
	@PostMapping
	@ResponseBody
	public String create(HttpServletRequest request, @RequestBody FournisseurDto fournisseur) throws com.fasterxml.jackson.core.JsonProcessingException {
		Utilisateur f;

		if (fournisseur.getMail().length() == 0) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Mail cannot be empty")
					);
		}

		try {
			f = utilisateurRepository.findByMail(fournisseur.getMail());
		} catch (Exception ex) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Unable to find user " + fournisseur.getMail(),
							ex)
					);
		}

		if (f != null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"User already exists")
					);
		} else {
			Fournisseur usr;
			Date date = new Date();
			try {
				boolean hasPassword = true;
				if (fournisseur.getPassword() == null || fournisseur.getPassword().length() == 0) {
					fournisseur.setPassword("null");
					hasPassword = false;
				}
				usr = new Fournisseur(fournisseur.getNom(), fournisseur.getPrenom(),
						fournisseur.getMail(), fournisseur.getAdresse(), fournisseur.getTelephone(), fournisseur.getMobile(),
						fournisseur.getFax(), bcryptEncoder.encode(fournisseur.getPassword()),
						date, fournisseur.getNumSiret(), fournisseur.getLogo(), fournisseur.getNomSociete(),
						fournisseur.getTypeEntreprise(), fournisseur.getMaisonMere(),
						fournisseur.getCodeAPE(), fournisseur.getCodeCPV(), fournisseur.getRaisonSociale(),
						fournisseur.getSiteInstitutionnel(), fournisseur.getDescription(), fournisseur.getEvaluations(), fournisseur.getDocuments());

				usr.setRole(roleRepository.findByName(RoleType.ROLE_FOURNISSEUR.toString()));


				fournisseurRepository.save(usr);
				usr = fournisseurRepository.findByMail(usr.getMail());
			
				VerificationToken verificationToken = new VerificationToken(usr);
				verificationTokenRepository.save(verificationToken);
				String port = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
				String path = (request.getServerPort() == 80) ? "/p-sigma" : "";
				if (hasPassword) {
					String activateUrl = String.format("%s://%s%s%s/home/%s",
							request.getScheme(),
							request.getServerName(),
							path,
							port,
							verificationToken.getToken());

					emailService.sendSimpleMessage(fournisseur.getMail(),
							"Activation compte SIGMA",
							"Votre inscription dans la plateforme SIGMA a été bien prise en compte. Veuillez cliquer sur ce <a href=\""+ activateUrl + "\">Lien d'activation </a> pour une confirmation.");
				} else {
					String choosePasswordUrl = String.format("%s://%s%s%s/new_password/%s",
							request.getScheme(),
							request.getServerName(),
							path,
							port,
							fournisseur.getMail());
					emailService.sendInscriptionMessage(usr.getMail(), choosePasswordUrl, "fournisseur");
				}

			} catch (Exception ex) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Unable to create user",
								ex)
						);
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							objectMapper.writeValueAsString(usr))
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
			Fournisseur user = fournisseurRepository.findOne(id);

			if (user != null) {
				List<Document> documents = user.getDocuments();
				Long userId = user.getId();

				VerificationToken token = verificationTokenRepository.findByUser((Utilisateur) user);
				if (token != null) {
					verificationTokenRepository.delete(token);
					System.out.println("Verification Token deleted successfully");
				} else {
					System.out.println("Verification Token not found");
				}


				fournisseurRepository.delete(user);

				for (Document d : documents) {
					storageService.delete(userId, d.getUrl());
				}
			} else {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"L'utilisateur n'existe pas")
						);
			}

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
	 * PUT /update  --> Update the user in the
	 * database having the passed id.
	 * Exemple: http://localhost:8080/fournisseur/update/6
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String updateUser(@RequestBody FournisseurDto fournisseur) throws com.fasterxml.jackson.core.JsonProcessingException {
		Fournisseur user;
		try {
			user = fournisseurRepository.findOne(fournisseur.getId());

			user.setNom(fournisseur.getNom());
			user.setPrenom(fournisseur.getPrenom());
			user.setAdresse(fournisseur.getAdresse());
			user.setTelephone(fournisseur.getTelephone());
			user.setNumSiret(fournisseur.getNumSiret());
			user.setNomSociete(fournisseur.getNomSociete());
			user.setCodeAPE(fournisseur.getCodeAPE());
			user.setCodeCPV(fournisseur.getCodeCPV());
			user.setRaisonSociale(fournisseur.getRaisonSociale());
			user.setSiteInstitutionnel(fournisseur.getSiteInstitutionnel());
			user.setDescription(fournisseur.getDescription());
			user.setTypeEntreprise(fournisseur.getTypeEntreprise());
			user.setMaisonMere(fournisseur.getMaisonMere());
			user.setFax(fournisseur.getFax());
			user.setMobile(fournisseur.getMobile());

			fournisseurRepository.save(user);
		}
		catch (Exception ex) {
			return "Error updating the user: " + ex.toString();
		}
		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(user))
				);
	}

	@PostMapping("/searchById")
	@ResponseBody
	public String getFournisseurById(@RequestParam Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Fournisseur usr = fournisseurRepository.findOne(id);

			if (usr == null) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Provider with id " + id + " does not exist")
						);
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							objectMapper.writeValueAsString(usr))
					);
		}
		catch (Exception ex) {
			return "Error updating the provider: " + ex.toString();
		}
	}

	@GetMapping("/motherCompany")
	@ResponseBody
	public String getFournisseurMotherCompany() throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Fournisseur> usrs = fournisseurRepository.findByTypeEntreprise(FournisseurType.TYPE_MAISON_MERE);

			return objectMapper.writeValueAsString(usrs);
		}
		catch (Exception ex) {
			return "Error listing the provider: " + ex.toString();
		}
	}

	/*
	 * Exemple : http://localhost:8080/fournisseur/get?mail=momo310795@gmail.com
	 */
	@PostMapping("/searchByMail")
	@ResponseBody
	public String getFournisseur(@RequestParam String mail) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			Fournisseur usr = fournisseurRepository.findByMail(mail);

			if (usr == null) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Provider " + mail + " does not exist")
						);
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							objectMapper.writeValueAsString(usr))
					);
		}
		catch (Exception ex) {
			return "Error updating the provider: " + ex.toString();
		}
	}
	/*
	 * Recherche de fournisseur par son nom de société
	 * http://localhost:8080/fournisseur/search?nom=Mairie+de+Rouen
	 */
	@PostMapping("/searchByName")
	@ResponseBody
	public String searchFournisseur(@RequestParam String companyName) throws com.fasterxml.jackson.core.JsonProcessingException {
		if (companyName == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Le paramètre 'companyName' n'est pas fourni")
					);
		}

		List<Fournisseur> fournisseurs = fournisseurRepository.findByNomSociete(companyName);

		return objectMapper.writeValueAsString(
				new ApiResponse(HttpStatus.OK,
						objectMapper.writeValueAsString(fournisseurs))
				);
	}

	/*
	 * Exemple : http://localhost:8080/fournisseur/byCPV?codecpv=03113200-8
	 */
	@GetMapping("/searchByCpv")
	@ResponseBody
	public String getFournisseurCPV(@RequestParam String codecpv) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Fournisseur> fournisseurs = fournisseurRepository.findByCodeCPVContaining(codecpv);

			if (fournisseurs == null) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Any providers with CodeCPV " + codecpv + " exist")
						);
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							objectMapper.writeValueAsString(fournisseurs))
					);
		}
		catch (Exception ex) {
			return "Error getting providers by CPV: " + ex.toString();
		}
	}
	@GetMapping("/byCpv/{codecpv}")
	@ResponseBody
	public String listFournisseurCPV(@PathVariable  String codecpv) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Fournisseur> fournisseurs = IterableToList.toList(fournisseurRepository.findAll());

			if (fournisseurs == null) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Any providers with CodeCPV " + codecpv + " exist")
						);
			}
			List<Fournisseur> fr = new ArrayList<Fournisseur>();
			for(Fournisseur fournisseur : fournisseurs) {
				if(fournisseur.containsCpv(codecpv)) {
					System.out.println("le fournisser " + fournisseur.getNom());
					fr.add(fournisseur);
					
				}
				
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							objectMapper.writeValueAsString(fr))
					);
		}
		catch (Exception ex) {
			return "Error getting providers by CPV: " + ex.toString();
		}
	}

	/*
	 * Exemple : http://localhost:8080/fournisseur/byAPE?codeape=0123Z
	 */
	@GetMapping("/searchByApe")
	@ResponseBody
	public String getFournisseurAPE(@RequestParam String codeape) throws com.fasterxml.jackson.core.JsonProcessingException {
		try {
			List<Fournisseur> fournisseurs = fournisseurRepository.findByCodeAPE(codeape);

			if (fournisseurs == null) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Any providers with CodeAPE " + codeape + " exists")
						);
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							objectMapper.writeValueAsString(fournisseurs))
					);
		}
		catch (Exception ex) {
			return "Error getting providers by APE: " + ex.toString();
		}
	}

  @RequestMapping("/contact/list")
  @ResponseBody
  public String listContacts(@RequestParam String mail) throws com.fasterxml.jackson.core.JsonProcessingException {
    try {
      Fournisseur fournisseur = fournisseurRepository.findByMail(mail);

      if (fournisseur == null) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
          "L'adresse e-mail spécifiée ne correspond à aucun fournisseur")
        );
      }

      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.OK,
        objectMapper.writeValueAsString(fournisseur.getContacts()))
      );

    } catch (Exception e) {
      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.BAD_REQUEST,
        "Erreur lors de la récupération des contacts",
        e)
      );
    }
  } 


  @RequestMapping("/contact/add")
  @ResponseBody
  public String addContact(@RequestParam String mail, @RequestBody ContactDto contact) throws com.fasterxml.jackson.core.JsonProcessingException {
    try {
      Fournisseur fournisseur = fournisseurRepository.findByMail(mail);

      if (fournisseur == null) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
          "L'adresse e-mail spécifiée ne correspond à aucun fournisseur")
        );
      }
      
      Contact c1 = contactRepository.findByMailAndFournisseur(contact.getMail(), fournisseur);
      
      if (c1 != null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Contact already exists")
					);
		}

      Contact c = new Contact(contact.getNom(),
                              contact.getPrenom(),
                              contact.getTelephone(),
                              contact.getMobile(),
                              contact.getFax(),
                              contact.getMail(),
                              contact.getAdresse());

      c.setFournisseur(fournisseur);

      fournisseur.addContact(c);

      contactRepository.save(c);
      fournisseurRepository.save(fournisseur);

      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.OK,
        "Le contact à bien été ajouté")
      );

    } catch (Exception e) {
      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.BAD_REQUEST,
        "Erreur lors de l'ajout du contact",
        e)
      );
    }
  }

  @RequestMapping("/contact/remove")
  @ResponseBody
  public String removeContact(@RequestParam String mail, @RequestParam Long contactId) throws com.fasterxml.jackson.core.JsonProcessingException {
    try {
      Fournisseur fournisseur = fournisseurRepository.findByMail(mail);

      if (fournisseur == null) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
          "L'adresse e-mail spécifiée ne correspond à aucun fournisseur")
        );
      }

      Optional<Contact> c = contactRepository.findById(contactId);
      Contact contact;

      if (c.isPresent()) {
        contact = c.get();
      } else {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
          "L'id spécifié ne correspond à aucun contact")
        );
      }

      if (!fournisseur.equals(contact.getFournisseur())) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
          "Le contact spécifié ne fait pas partie de la liste des contacts de ce fournisseur")
        );
      }

      fournisseur.removeContact(contact);
      contactRepository.delete(contact);
      fournisseurRepository.save(fournisseur);

      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.OK,
        "Le contact à bien été retiré")
      );

    } catch (Exception e) {
      return objectMapper.writeValueAsString(
        new ApiResponse(HttpStatus.BAD_REQUEST,
        "Erreur lors de la suppression du contact",
        e)
      );
    }
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

		if (user.getRole() == null || user.getRole().getName().equals("ROLE_FOURNISSEUR")) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.EXPECTATION_FAILED,
							"Insufficient privileges")
					);
		}
		
		List<Fournisseur> fournisseurs = IterableToList.toList(fournisseurRepository.findAll());
		String path = "";
		try {
			path = excelService.createFile(fournisseurs);
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


	//Private fields

	@Autowired
	private FournisseurRepository fournisseurRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	StorageService storageService;

	@Autowired
	DocumentRepository documentRepository;

	@Autowired
	VerificationTokenRepository verificationTokenRepository;

	@Autowired
	EmailServiceImpl emailService;
	
	@Autowired
	FournisseurExcelServiceImpl excelService;
	
	  @Autowired
  	ContactRepository contactRepository;
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	@Autowired
	JwtTokenUtil tokenUtil;
}
