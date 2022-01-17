package com.ninjaone.serverapp;

import static org.assertj.core.api.Assertions.assertThat;

import com.ninjaone.serverapp.controllers.CustomerController;
import com.ninjaone.serverapp.controllers.CustomerDeviceController;
import com.ninjaone.serverapp.controllers.CustomerServiceController;
import com.ninjaone.serverapp.controllers.JwtAuthenticationController;
import com.ninjaone.serverapp.controllers.ServiceCostController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ServerAppApplication.class)
class ServerAppControllerExistenceTests {

  @Autowired
  private CustomerController customerController;

  @Autowired
  private CustomerDeviceController customerDeviceController;

  @Autowired
  private CustomerServiceController customerServiceController;

  @Autowired
  private ServiceCostController serviceCostController;

  @Autowired
  private JwtAuthenticationController jwtAuthenticationController;

  @Test
  public void contextLoads() {
    assertThat(customerController).isNotNull();
    assertThat(customerDeviceController).isNotNull();
    assertThat(customerServiceController).isNotNull();
    assertThat(serviceCostController).isNotNull();
    assertThat(jwtAuthenticationController).isNotNull();
  }
}
