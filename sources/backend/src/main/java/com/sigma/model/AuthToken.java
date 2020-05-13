package com.sigma.model;

import com.sigma.utilisateur.Utilisateur;

public class AuthToken {

    private String token;
    private Utilisateur user;

    public AuthToken() {

    }

    public AuthToken(String token, Utilisateur user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Utilisateur getUser() {
        return this.user;
    }

    public void setUser(Utilisateur u) {
        this.user = u;
    }

}
