package com.sigma.service.impl;

import com.sigma.dto.UtilisateurDto;
import com.sigma.service.UserService;
import com.sigma.utilisateur.Utilisateur;
import com.sigma.utilisateur.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UtilisateurRepository userDao;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur user = userDao.findByMail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.\n Username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getMail(), user.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public List<Utilisateur> findAll() {
        List<Utilisateur> list = new ArrayList<>();
        userDao.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public void delete(long id) {
        userDao.delete(id);
    }

    @Override
    public Utilisateur findOne(String mail) {
        return userDao.findByMail(mail);
    }

    @Override
    public Utilisateur findById(Long id) {
        return userDao.findOne(id);
    }

    @Override
    public Utilisateur save(UtilisateurDto user) {
        Utilisateur newUser = new Utilisateur();
        newUser.setNom(user.getNom());
        newUser.setPrenom(user.getPrenom());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setMail(user.getMail());
        newUser.setAdresse(user.getAdresse());
        return userDao.save(newUser);
    }
}
