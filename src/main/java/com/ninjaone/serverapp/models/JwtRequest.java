package com.ninjaone.serverapp.models;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a JWT request with a provided username and password.
 */
public class JwtRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String username;
  private String password;

  // Default JWT constructor used for parsing the JSON.
  public JwtRequest() {

  }

  public JwtRequest(String username, String password) {
    this.setUsername(username);
    this.setPassword(password);
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
