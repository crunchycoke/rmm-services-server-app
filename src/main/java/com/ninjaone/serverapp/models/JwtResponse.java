package com.ninjaone.serverapp.models;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a JWT response with a token included.
 */
public class JwtResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  
  private final String jwtToken;

  public JwtResponse(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public String getToken() {
    return this.jwtToken;
  }
}
