package com.sigma.auth;

import com.sigma.config.JwtTokenUtil;
import com.sigma.model.Acheteur;
import com.sigma.model.AdministrateurEntite;
import com.sigma.model.AdministrateurSigma;
import com.sigma.model.ApiResponse;
import com.sigma.model.AuthToken;
import com.sigma.model.Fournisseur;
import com.sigma.model.LoginUser;
import com.sigma.model.ResponsableAchat;
import com.sigma.model.Visiteur;
import com.sigma.utilisateur.Utilisateur;
import com.sigma.utilisateur.UtilisateurRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UtilisateurRepository userService;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @CrossOrigin
    @PostMapping("/sign-in")
    public ResponseEntity<?> register(@RequestBody LoginUser loginUser) throws AuthenticationException, com.fasterxml.jackson.core.JsonProcessingException {

      try {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getMail(),
                        loginUser.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final Utilisateur user = userService.findByMail(loginUser.getMail());
       
        if (user.getRole().getName().equals("ROLE_FOURNISSEUR") && user instanceof Fournisseur) {
          Fournisseur usr = (Fournisseur) user;

          if (!usr.isEnabled()) {
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(
                        new ApiResponse(HttpStatus.EXPECTATION_FAILED,
                                        "L'utilisateur n'est pas activé")
                    ), HttpStatus.OK);
          }
        }
        
        


        final String token = jwtTokenUtil.generateToken(user);

        if (user instanceof Fournisseur) {
            // return ResponseEntity.ok(new AuthToken(token, (Fournisseur) user));
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(
                        new ApiResponse(HttpStatus.OK,
                                          objectMapper.writeValueAsString(new AuthToken(token, (Fournisseur) user)))
                    ), HttpStatus.OK);
        } 
        if (user instanceof ResponsableAchat) {
            
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(
                        new ApiResponse(HttpStatus.OK,
                                          objectMapper.writeValueAsString(new AuthToken(token, (ResponsableAchat) user)))
                    ), HttpStatus.OK);
        } 
        if (user instanceof Visiteur) {
            
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(
                        new ApiResponse(HttpStatus.OK,
                                          objectMapper.writeValueAsString(new AuthToken(token, (Visiteur) user)))
                    ), HttpStatus.OK);
        } 
        if (user instanceof AdministrateurSigma) {
        	
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(
                        new ApiResponse(HttpStatus.OK,
                                          objectMapper.writeValueAsString(new AuthToken(token, (AdministrateurSigma) user)))
                    ), HttpStatus.OK);
        } 
        if (user instanceof AdministrateurEntite) {
            // return ResponseEntity.ok(new AuthToken(token, (Fournisseur) user));
        	AdministrateurEntite usr = (AdministrateurEntite) user;

            if (usr.getEntite() == null) {
              return new ResponseEntity<String>(
                      objectMapper.writeValueAsString(
                          new ApiResponse(HttpStatus.EXPECTATION_FAILED,
                                          "Vous n'avez pas encore d'entité à gérer.")
                      ), HttpStatus.OK);
            }
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(
                        new ApiResponse(HttpStatus.OK,
                                          objectMapper.writeValueAsString(new AuthToken(token, usr)))
                    ), HttpStatus.OK);
        } 
        if (user instanceof Acheteur) {
            // return ResponseEntity.ok(new AuthToken(token, (Fournisseur) user));
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(
                        new ApiResponse(HttpStatus.OK,
                                          objectMapper.writeValueAsString(new AuthToken(token, (Acheteur) user)))
                    ), HttpStatus.OK);
        } else {
            // return ResponseEntity.ok(new AuthToken(token, (Fournisseur) user));
            return new ResponseEntity<String>(
                    objectMapper.writeValueAsString(
                        new ApiResponse(HttpStatus.OK,
                                          objectMapper.writeValueAsString(new AuthToken(token, user)))
                    ), HttpStatus.OK);
        } 

      } catch(BadCredentialsException e) {
        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(
                    new ApiResponse(HttpStatus.BAD_REQUEST,
                                    "E-Mail / Mot de passe invalide")
                ), HttpStatus.OK);
      } catch(AuthenticationException e) {
    	  e.printStackTrace();
        return new ResponseEntity<String>(
                objectMapper.writeValueAsString(
                    new ApiResponse(HttpStatus.BAD_REQUEST,
                                    "Authentication Exception",
                                    e)
                ), HttpStatus.OK);
      }
    }

}
