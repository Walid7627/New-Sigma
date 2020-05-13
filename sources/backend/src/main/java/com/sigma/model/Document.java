package com.sigma.model;

import javax.persistence.*;

@Entity
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String url;
  private String name;

  public Document() { }

  public Document(String url, String name) {
    this.url = url;
    this.name = name;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
