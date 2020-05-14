package com.sigma.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Qualification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int ca1;
    private int ca2;
    private int ca3;

    private int ebe1;
    private int ebe2;
    private int ebe3;

    private int cp1;
    private int cp2;
    private int cp3;

    private int va1;
    private int va2;
    private int va3;

    private int rn1;
    private int rn2;
    private int rn3;

    private int cs;


    public Qualification() {
        // TODO Auto-generated constructor stub
    }

    public Qualification(int c1, int c2, int c3, int e1, int e2, int e3,
                         int r1, int r2, int r3, int cp1, int cp2, int cp3,
                         int v1, int v2, int v3, int cs) {
        this.ca1 = c1;
        this.ca2 = c2;
        this.ca3 = c3;
        this.ebe1 = e1;
        this.ebe2 = e2;
        this.ebe3 = e3;
        this.rn1 = r1;
        this.rn2 = r2;
        this.rn3 = r3;
        this.cp1 = cp1;
        this.cp2 = cp2;
        this.cp3 = cp3;
        this.va1 = v1;
        this.va2 = v2;
        this.va3 = v3;
        this.cs = cs;
    }

    public Long getId() { return id; }

    public int getCa1() {
        return ca1;
    }
    public int getCa2() {
        return ca2;
    }
    public int getCa3() {
        return ca3;
    }

    public int getEbe1() {
        return ebe1;
    }
    public int getEbe2() {
        return ebe2;
    }
    public int getEbe3() {
        return ebe3;
    }

    public int getVa3() {
        return va3;
    }

    public int getVa2() {
        return va2;
    }

    public int getVa1() {
        return va1;
    }

    public int getRn3() {
        return rn3;
    }

    public int getRn2() {
        return rn2;
    }

    public int getRn1() {
        return rn1;
    }

    public int getCs() {
        return cs;
    }

    public int getCp3() {
        return cp3;
    }

    public int getCp2() {
        return cp2;
    }

    public int getCp1() {
        return cp1;
    }


    public void setCa1(int ca) {
        this.ca1 = ca;
    }
    public void setCa2(int ca) {
        this.ca2 = ca;
    }
    public void setCa3(int ca) { this.ca3 = ca; }

    public void setEbe1(int ca) { this.ebe1 = ca; }
    public void setEbe2(int ca) { this.ebe2 = ca; }
    public void setEbe3(int ca) { this.ebe3 = ca; }

    public void setCp1(int cap_prop1) {
        this.cp1 = cap_prop1;
    }

    public void setCp2(int cap_prop2) {
        this.cp2 = cap_prop2;
    }

    public void setCp3(int cap_prop3) {
        this.cp3 = cap_prop3;
    }

    public void setCs(int cap_soc) {
        this.cs = cap_soc;
    }

    public void setRn1(int res_net1) {
        this.rn1 = res_net1;
    }

    public void setRn2(int res_net2) {
        this.rn2 = res_net2;
    }

    public void setRn3(int res_net3) {
        this.rn3 = res_net3;
    }

    public void setVa1(int val_ajo1) {
        this.va1 = val_ajo1;
    }

    public void setVa2(int val_ajo2) {
        this.va2 = val_ajo2;
    }

    public void setVa3(int val_ajo3) {
        this.va3 = val_ajo3;
    }



}

