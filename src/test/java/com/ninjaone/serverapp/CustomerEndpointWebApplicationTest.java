package com.ninjaone.serverapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.serverapp.enums.DeviceType;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.models.CustomerDevice;
import com.ninjaone.serverapp.models.JwtRequest;
import com.ninjaone.serverapp.testmodels.IntegrationTestCustomer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

  @Test
  public void accessAllCustomerRecordsSuccess() throws Exception {
    JwtRequest jwtRequest = new JwtRequest("admin", "admin");
    String jwtRequestValue = mapper.writeValueAsString(jwtRequest);

    MvcResult mvcResult = mockMvc.perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestValue))
        .andExpect(status().is2xxSuccessful())
        .andReturn();

    String authToken = mvcResult.getResponse().getContentAsString();
    String token = authToken.split("\":\"")[1];
    token = token.substring(0, token.length() - 2);

    mockMvc.perform(
            get("/customers")
                .header("authorization", "Bearer " + token))
        .andExpect(status().is2xxSuccessful())
        .andReturn();
  }

  @Test
  public void accessExistingCustomerRecordSuccess() throws Exception {
    JwtRequest jwtRequest = new JwtRequest("admin", "admin");
    String jwtRequestValue = mapper.writeValueAsString(jwtRequest);

    MvcResult mvcResult = mockMvc.perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestValue))
        .andExpect(status().is2xxSuccessful())
        .andReturn();

    String authToken = mvcResult.getResponse().getContentAsString();
    String token = authToken.split("\":\"")[1];
    token = token.substring(0, token.length() - 2);

    mockMvc.perform(
            get("/customers/1")
                .header("authorization", "Bearer " + token))
        .andExpect(status().is2xxSuccessful())
        .andReturn();
  }

  @Test
  public void createDeviceSuccess() throws Exception {
    JwtRequest jwtRequest = new JwtRequest("admin", "admin");
    String jwtRequestValue = mapper.writeValueAsString(jwtRequest);

    MvcResult mvcResult = mockMvc.perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestValue))
        .andExpect(status().is2xxSuccessful())
        .andReturn();

    String authToken = mvcResult.getResponse().getContentAsString();
    String token = authToken.split("\":\"")[1];
    token = token.substring(0, token.length() - 2);

    Customer customer = initializeCustomerData("admin");
    CustomerDevice customerDevice = new CustomerDevice(1000L,
        "Windows Server Machine", DeviceType.WINDOWS_SERVER, customer);
    String customerDeviceValue = mapper.writeValueAsString(customerDevice);

    mockMvc.perform(
            post("/customers/1/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDeviceValue)
                .header("authorization", "Bearer " + token))
        .andExpect(status().is2xxSuccessful())
        .andReturn();
  }

  @Test
  public void accessAllDevicesSuccess() throws Exception {
    JwtRequest jwtRequest = new JwtRequest("admin", "admin");
    String jwtRequestValue = mapper.writeValueAsString(jwtRequest);

    MvcResult mvcResult = mockMvc.perform(
            post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jwtRequestValue))
        .andExpect(status().is2xxSuccessful())
        .andReturn();

    String authToken = mvcResult.getResponse().getContentAsString();
    String token = authToken.split("\":\"")[1];
    token = token.substring(0, token.length() - 2);

    mockMvc.perform(
            get("/customers/devices")
                .header("authorization", "Bearer " + token))
        .andExpect(status().is2xxSuccessful())
        .andReturn();
  }

  @Test
  public void createCustomerInvalidDataFail() throws Exception {
    Customer customer = initializeCustomerData("");

    String customerValue = mapper.writeValueAsString(customer);

    mockMvc.perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerValue))
        .andExpect(status().is4xxClientError())
        .andReturn();
  }
}
