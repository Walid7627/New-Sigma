package com.sigma.config;

import com.sigma.model.Privilege;
import com.sigma.model.Role;
import com.sigma.model.RoleType;
import com.sigma.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RoleConfig
implements ApplicationListener<ApplicationReadyEvent> {

  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {

    /*

    Role Fournisseur

    */

    List<Privilege> privileges = new ArrayList<Privilege>();
    privileges.addAll(Arrays.asList(new Privilege[] {
      Privilege.ADD_OWN_DOCUMENT,
      Privilege.DELETE_OWN_DOCUMENT,
      Privilege.GET_OWN_DOCUMENT
    }));

    String name = RoleType.ROLE_FOURNISSEUR.toString();
    Role r = new Role(name, privileges);

    if(roleRepository.findByName(name) == null) {
      roleRepository.save(r);
    }

    /*

    Role acheteur

    */

    privileges = new ArrayList<Privilege>();
    privileges.addAll(Arrays.asList(new Privilege[] {
      Privilege.ADD_FOURNISSEUR,
      Privilege.DELETE_FOURNISSEUR,
      Privilege.GET_FOURNISSEUR,
      Privilege.GET_OTHER_DOCUMENT
    }));

    name = RoleType.ROLE_ACHETEUR.toString();
    r = new Role(name, privileges);

    if(roleRepository.findByName(name) == null) {
      roleRepository.save(r);
    }

    /*

    Role administrateur entité

    */

    privileges = new ArrayList<Privilege>();
    privileges.addAll(Arrays.asList(new Privilege[] {
      Privilege.ADD_ACHETEUR,
      Privilege.DELETE_ACHETEUR,
      Privilege.GET_ACHETEUR,
      Privilege.CREATE_TEAM,
      Privilege.DELETE_TEAM,
      Privilege.GET_TEAM,
      Privilege.CREATE_SEGMENT,
      Privilege.DELETE_SEGMENT,
      Privilege.GET_SEGMENT,
      Privilege.GET_OTHER_DOCUMENT
    }));


    name = RoleType.ROLE_ADMINISTRATEUR_ENTITE.toString();
    r = new Role(name, privileges);

    if(roleRepository.findByName(name) == null) {
      roleRepository.save(r);
    }
    
    privileges = new ArrayList<Privilege>();
    privileges.addAll(Arrays.asList(new Privilege[] {
      Privilege.ADD_ADMINISTRATEUR_ENTITE,
      Privilege.DELETE_ADMINISTRATEUR_ENTITE,
      Privilege.GET_ADMINISTRATEUR_ENTITE,
      Privilege.ADD_ENTITE,
      Privilege.DELETE_ENTITE,
      Privilege.GET_ENTITE,
      Privilege.GET_OTHER_DOCUMENT
    }));
    
    name = RoleType.ROLE_ADMINISTRATEUR_SIGMA.toString();
    r = new Role(name, privileges);
    
    if(roleRepository.findByName(name) == null) {
        roleRepository.save(r);
    }
  //Visiteur
    
    name = RoleType.ROLE_VISITEUR.toString();
    privileges = new ArrayList<Privilege>();
    privileges.addAll(Arrays.asList(new Privilege[] {
  		  Privilege.ADD_VISITEUR,
  	      Privilege.DELETE_VISITEUR,
  	      Privilege.GET_VISITEUR
    }));
    
    name = RoleType.ROLE_VISITEUR.toString();
    r = new Role(name, privileges);

    if(roleRepository.findByName(name) == null) {
      roleRepository.save(r);
    }
    
    // Responsable achat
    
    name = RoleType.ROLE_RESPONSABLE_ACHAT.toString();
    privileges = new ArrayList<Privilege>();
    privileges.addAll(Arrays.asList(new Privilege[] {
      Privilege.JOIN_SEGMENT,
      Privilege.UNJOIN_SEGMENT
    }));
    
    r = new Role(name, privileges);
    if(roleRepository.findByName(name) == null) {
        roleRepository.save(r);
    }
    return;
  }


  
  @Autowired
  private RoleRepository roleRepository;


}
