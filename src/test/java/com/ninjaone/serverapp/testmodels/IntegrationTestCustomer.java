package com.ninjaone.serverapp.testmodels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntegrationTestCustomer {

  @JsonProperty(access = JsonProperty.Access.READ_WRITE)
  private String password;
}
