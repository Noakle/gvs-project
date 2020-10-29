package de.hhn.it.vs.common.core.usermanagement;

import java.io.Serializable;

/**
 * Created by wnck on 11/03/2017.
 */

public class User implements Serializable {
  private Token token;
  private String name;
  private String email;


  /**
   * Default constructor, needed for REST JSON deserialization.
   */
  public User() {
  }

  public User(final String name, final String email) {
    this.name = name;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public Token getToken() {
    return token;
  }

  public void setToken(final Token token) {
    this.token = token;
  }

  @Override
  public String toString() {
    return "User{" +
            "token=" + token +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            '}';
  }
}
