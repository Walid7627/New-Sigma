package com.sigma.utilisateur;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigma.dto.UtilisateurDto;
import com.sigma.model.LoginUser;
import com.sigma.model.UserType;
import com.sigma.model.Fournisseur;
import com.sigma.util.IterableToList;


@Controller
@RequestMapping("/api/users")
public class UtilisateurController {

    @GetMapping
    @ResponseBody
    public String list() {
        try {
            List<Utilisateur> users = IterableToList.toList(utilisateurRepository.findAll());
            String jsonString = objectMapper.writeValueAsString(users);
            return jsonString;
        } catch (Exception ex) {
            return "Error retrieving users: " + ex.toString();
        }
    }

    /**
     * GET /create  --> Create a new user and save it in the database.
     */
    @PostMapping
    @ResponseBody
    public String create(@RequestBody UtilisateurDto user) {
        String userId = "";
        try {

      /*
      Utilisateur(String nom, String prenom, String mail, Address adresse,
                          String password, String dateEnregistrement, String userType)
      */

            Utilisateur usr = new Utilisateur(user.getNom(), user.getPrenom(),
                    user.getMail(), user.getAdresse(), user.getTelephone(), user.getMobile(), bcryptEncoder.encode(user.getPassword()),
                    new Date(), UserType.USER);

            utilisateurRepository.save(usr);
            userId = String.valueOf(usr.getId());
        } catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }
        return "User succesfully created with id = " + userId;
    }

    /**
     * GET /delete  --> Delete the user having the passed id.
     */
    @DeleteMapping
    @ResponseBody
    public String delete(@RequestParam Long id) {
        try {
            Utilisateur user = utilisateurRepository.findOne(id);
            utilisateurRepository.delete(user);
        } catch (Exception ex) {
            return "Error deleting the user:" + ex.toString();
        }
        return "User succesfully deleted!";
    }


    /**
     * GET /update  --> Update the email and the name for the user in the
     * database having the passed id.
     */
    @PutMapping
    @ResponseBody
    public String updateUser(Long id, String firstName, String lastName) {
        try {
            Utilisateur user = utilisateurRepository.findOne(id);
            user.setPrenom(firstName);
            user.setNom(lastName);
            utilisateurRepository.save(user);
        } catch (Exception ex) {
            return "Error updating the user: " + ex.toString();
        }
        return "User succesfully updated!";
    }
    
    @SuppressWarnings("rawtypes")
	@PostMapping("/password")
    @ResponseBody
    public ResponseEntity modifyPassword(@RequestBody LoginUser loginUser){
        Utilisateur user = utilisateurRepository.findByMail(loginUser.getMail());
        if(user == null){
            return ResponseEntity.status(404).body("User not found");
        }
        try{
	    if(user instanceof Fournisseur) {
		Fournisseur f = (Fournisseur) user;
		f.setEnabled();
		user = f;
	    }
            user.setPassword(bcryptEncoder.encode(loginUser.getPassword()));
            utilisateurRepository.save(user);
            return ResponseEntity.status(200).body("Password successfully updated.");
        } catch (Exception e){
            return ResponseEntity.status(500).body("Unknown error : password not updated.");
        }
    }

    // Private fields

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;
}
