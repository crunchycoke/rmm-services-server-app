package com.ninjaone.serverapp.services;

import com.ninjaone.serverapp.enums.DeviceOperatingSystem;
import com.ninjaone.serverapp.enums.DeviceType;
import com.ninjaone.serverapp.exceptions.CustomerNotFoundException;
import com.ninjaone.serverapp.exceptions.ServiceCostNotFoundException;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.models.CustomerBill;
import com.ninjaone.serverapp.models.CustomerDevice;
import com.ninjaone.serverapp.models.CustomerService;
import com.ninjaone.serverapp.repository.CustomerDeviceRepository;
import com.ninjaone.serverapp.repository.CustomerRepository;
import com.ninjaone.serverapp.repository.CustomerServiceRepository;
import com.ninjaone.serverapp.repository.ServiceCostRepository;
import com.ninjaone.serverapp.services.interfaces.CustomerAccessService;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class CustomerRecordDetailsService implements CustomerAccessService {

  private static final Logger log =
      LoggerFactory.getLogger(CustomerRecordDetailsService.class);

  private final CustomerRepository customerRepository;
  private final CustomerDeviceRepository customerDeviceRepository;
  private final CustomerServiceRepository customerServiceRepository;
  private final ServiceCostRepository serviceCostRepository;

  /**
   * Constructs the service used for accessing the records and details within the customer table.
   *
   * @param customerRepository        Represents the repository used for accessing the customer
   *                                  table loaded through dependency injection.
   * @param customerDeviceRepository  Represents the repository used for accessing the device table
   *                                  loaded through dependency injection.
   * @param customerServiceRepository Represents the repository used for accessing the service table
   *                                  loaded through dependency injection.
   * @param serviceCostRepository     Represents the repository used for accessing the service cost
   *                                  table loaded through dependency injection.
   */
  public CustomerRecordDetailsService(CustomerRepository customerRepository,
      CustomerDeviceRepository customerDeviceRepository,
      CustomerServiceRepository customerServiceRepository,
      ServiceCostRepository serviceCostRepository) {
    this.customerRepository = customerRepository;
    this.customerDeviceRepository = customerDeviceRepository;
    this.customerServiceRepository = customerServiceRepository;
    this.serviceCostRepository = serviceCostRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Customer> getCustomers() {
    List<Customer> customers = customerRepository.findAll();

    log.info("Retrieved all available customer devices.");

    return customers;
  }

  /**
   * Authenticates the provided authentication information and customer ID. Checks whether the
   * provided token matches the customer attached to the ID provided.
   *
   * @param authentication Represents the authentication token.
   * @param id             Represents the customer ID provided for authentication.
   * @return Represents the Customer authenticated.
   */
  public Customer authenticateCustomerId(Authentication authentication, Long id) {
    String username = authentication.getName();

    Customer customer = customerRepository.getCustomerByIdAndUsername(id, username)
        .orElseThrow(() -> new CustomerNotFoundException(id));

    log.info("Finished authentication process.");

    return customer;
  }

  /**
   * Calculates the customer bill using the provided customer ID and retrieves the linked devices
   * and services under the customer ID.
   *
   * @param id Represents the customer ID.
   * @return The customer bill object containing the calculated amounts and prices.
   */
  public CustomerBill calculateCustomerBill(Long id) {
    List<CustomerDevice> customerDevices =
        customerDeviceRepository.getCustomerDevicesByCustomerId(id);

    List<CustomerService> customerServices =
        customerServiceRepository.getCustomerServicesByCustomerId(id);

    CustomerBill customerBill = new CustomerBill();

    for (CustomerDevice customerDevice : customerDevices) {
      customerBill.addDeviceToTotalCost();

      DeviceOperatingSystem selectedDeviceOperatingSystem;

      if (DeviceType.isWindowsDevice(customerDevice.getDeviceType())) {
        selectedDeviceOperatingSystem = DeviceOperatingSystem.WINDOWS;
        customerBill.incrementWindowsDeviceCount();
      } else {
        selectedDeviceOperatingSystem = DeviceOperatingSystem.MAC;
        customerBill.incrementMacDeviceCount();
      }

      for (CustomerService customerService : customerServices) {
        BigDecimal determinedServicePrice =
            serviceCostRepository.getServicePriceByTypeAndOs(
                    customerService.getServiceType(),
                    selectedDeviceOperatingSystem)
                .orElseThrow(() ->
                    new ServiceCostNotFoundException(customerService.getServiceType()));

        customerBill.setTotalCost(customerBill.getTotalCost().add(determinedServicePrice));
      }
    }

    return customerBill;
  }
}
