package com.sigma.model;

import javax.persistence.*;

@Entity
@Table(name = "adresse")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "number")
    private String number;
    @Column(name = "street")
    private String street;
    @Column(name = "postalcode")
    private String postalCode;
    @Column(name = "city")
    private String city;
    @Column(name = "country")
    private String country;

    public Address() {
    }

    public Address(String number, String street, String postalCode, String city,
                   String country) {
        this.number = number;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public String getNumber() {
        return number == null ? "" : number;
    }

    public String getStreet() {
        return this.street;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getCity() {
        return this.city;
    }

    public String getCountry() {
        return country == null ? "" : country;
    }

    @Override
    public String toString() {
        return getNumber() + " " + this.street + "  " + this.postalCode + " - " +
                this.city + "  " + getCountry();
    }

}
