package com.sigma.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @ElementCollection(targetClass=Privilege.class)
  @Enumerated(EnumType.STRING)
  @CollectionTable(name="role_privilege")
  @Column(name="privileges")
  private List<Privilege> privileges;

  public Role() {}

  public Role(String name, List<Privilege> privileges) {
    this.name = name;
    this.privileges = privileges;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Privilege> getPrivileges() {
    return this.privileges;
  }

  public void addPrivilege(Privilege privilege) {
    this.privileges.add(privilege);
  }

  public void removePrivilege(Privilege privilege) {
    this.privileges.remove(privilege);
  }
}
