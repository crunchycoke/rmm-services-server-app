package com.ninjaone.serverapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.models.JwtRequest;
import com.ninjaone.serverapp.testmodels.IntegrationTestCustomer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerEndpointWebApplicationTest {

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper mapper = new ObjectMapper();

  private Customer initializeCustomerData(String username) {
    Customer customer = new Customer("First",
        "Middle",
        "Last",
        username,
        "admin");

    mapper.addMixIn(Customer.class, IntegrationTestCustomer.class);

    return customer;
  }

  @Test
  public void createCustomerSuccess() throws Exception {
    Customer customer = initializeCustomerData("admin");

    String customerValue = mapper.writeValueAsString(customer);

    mockMvc.perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerValue))
        .andExpect(status().is2xxSuccessful())
        .andReturn();
  }

  @Test
  public void authorizeCustomerSuccess() throws Exception {
    JwtRequest jwtRequest = new JwtRequest("admin", "admin");
    String jwtRequestValue = mapper.writeValueAsString(jwtRequest);

    mockMvc.perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestValue))
        .andExpect(status().is2xxSuccessful())
        .andReturn();
  }
}
