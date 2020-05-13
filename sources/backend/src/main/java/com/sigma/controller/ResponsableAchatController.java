package com.sigma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.model.Acheteur;
import com.sigma.model.ApiResponse;
import com.sigma.model.ResponsableAchat;
import com.sigma.model.Segment;
import com.sigma.repository.AcheteurRepository;
import com.sigma.repository.ResponsableAchatRepository;
import com.sigma.repository.RoleRepository;
import com.sigma.repository.SegmentRepository;
import com.sigma.util.IterableToList;



@Controller
@RequestMapping("/api/responsable")
public class ResponsableAchatController {

    @GetMapping
    @ResponseBody
    public String list() throws com.fasterxml.jackson.core.JsonProcessingException {
        try {
            List<ResponsableAchat> users = IterableToList.toList(responsableAchatRepository.findAll());
            return objectMapper.writeValueAsString(users);
        } catch (Exception ex) {
          return objectMapper.writeValueAsString(
            new ApiResponse(HttpStatus.BAD_REQUEST,
                            "Unable to find users",
                            ex)
          );
        }
    }
   
    

    /**
     * GET /delete  --> Delete the user having the passed id.
     */
    @DeleteMapping
    @ResponseBody
    public String delete(@RequestParam Long id) throws com.fasterxml.jackson.core.JsonProcessingException {
        try {
            ResponsableAchat user = responsableAchatRepository.findOne(id);
            responsableAchatRepository.delete(user);
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
     * GET /update  --> Update the email and the name for the user in the
     * database having the passed id.
     */
    @PutMapping
    @ResponseBody
    public String updateUser(@RequestParam Long id, @RequestBody ResponsableAchat responsableAchat) throws com.fasterxml.jackson.core.JsonProcessingException {
        try {
        	ResponsableAchat usr = new ResponsableAchat(responsableAchat.getNom(), responsableAchat.getPrenom(),
                    responsableAchat.getMail(), responsableAchat.getAdresse(), responsableAchat.getTelephone(),
                    responsableAchat.getMobile(),
                    responsableAchat.getDateEnregistrement(), responsableAchat.getEquipe(), responsableAchat.getEntite(), responsableAchat.getSegments());
        	
        	
        	ResponsableAchat res = responsableAchatRepository.findOne(id);
        	usr.setRole(res.getRole());
        	responsableAchatRepository.delete(res);
        	responsableAchatRepository.save(usr);
        	
        } catch (Exception ex) {
           return "Error updating the user: " + ex.toString();
        }
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.OK,
                          "User successfully updated")
        );
    }
    
    @PutMapping("/joinSegment")
	@ResponseBody
	public String joinSegment(@RequestParam Long sid, @RequestParam Long aid) throws com.fasterxml.jackson.core.JsonProcessingException {

		Acheteur ach = acheteurRepository.findOne(aid);

		Segment seg = segmentRepository.findOne(sid);

		if (seg == null || ach == null) {
			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.BAD_REQUEST,
							"Segment ou acheteur inexistant")
					);
		} else {
			
			try {
				Acheteur a = seg.getAcheteur();
				if (a != null) {
					a.removeSegment(seg);
				}
				seg.setAcheteur(ach);
				ach.addSegment(seg);
			} catch (Exception ex) {
				return objectMapper.writeValueAsString(
						new ApiResponse(HttpStatus.BAD_REQUEST,
								"Unable to add segment to pourchaser",
								ex)
						);
			}

			return objectMapper.writeValueAsString(
					new ApiResponse(HttpStatus.OK,
							"Segment successfully added to purchaser\nReceived input:\n")
					);
		}
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

   		ResponsableAchat ach = responsableAchatRepository.findOne(id);

   		return objectMapper.writeValueAsString(
   				new ApiResponse(HttpStatus.OK,
   						objectMapper.writeValueAsString(ach))
   				);
   	}

    // Private fields

    @Autowired
    private ResponsableAchatRepository responsableAchatRepository;
    
    @Autowired
	private AcheteurRepository acheteurRepository;
    
    @Autowired
	private SegmentRepository segmentRepository;
    
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    
    @Autowired
    RoleRepository roleRepository;
    
    
    /**
     * GET /create  --> Create a new user and save it in the database.
     */
   /* @RequestMapping("/create")
    @ResponseBody
    public String create(@RequestBody ResponsableAchatDto responsableAchat) throws com.fasterxml.jackson.core.JsonProcessingException {
      Utilisateur a;

      if (responsableAchat.getMail().length() == 0) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
                          "Mail cannot be empty")
        );
      }

      if (responsableAchat.getPassword().length() == 0) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
                          "Password cannot be empty")
        );
      }

      try {
        a = utilisateurRepository.findByMail(responsableAchat.getMail());
      } catch (Exception ex) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
                          "Unable to find user " + responsableAchat.getMail(),
                          ex)
        );
      }

      if (a != null) {
        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.BAD_REQUEST,
                          "User already exists")
        );
      } else {
        // String userId = "";

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        try {
            ResponsableAchat usr = new ResponsableAchat(responsableAchat.getNom(), responsableAchat.getPrenom(),
                    responsableAchat.getMail(), responsableAchat.getAdresse(), responsableAchat.getTelephone(),
                    bcryptEncoder.encode(responsableAchat.getPassword()),
                    dateFormat.format(date), responsableAchat.getEquipe(), responsableAchat.getSegment());

            responsableAchatRepository.save(usr);
            // userId = String.valueOf(usr.getId());
        } catch (Exception ex) {
          return objectMapper.writeValueAsString(
            new ApiResponse(HttpStatus.BAD_REQUEST,
                            "Unable to create user",
                            ex)
          );
        }

        return objectMapper.writeValueAsString(
          new ApiResponse(HttpStatus.OK,
                          "User successfully created\nReceived input:\n" + objectMapper.writeValueAsString(responsableAchat))
        );
      }
    }*/
}
