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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CustomerRecordDetailsService implements CustomerAccessService {

  private final CustomerRepository customerRepository;
  private final CustomerDeviceRepository customerDeviceRepository;
  private final CustomerServiceRepository customerServiceRepository;
  private final ServiceCostRepository serviceCostRepository;

  public CustomerRecordDetailsService(CustomerRepository customerRepository,
      CustomerDeviceRepository customerDeviceRepository,
      CustomerServiceRepository customerServiceRepository,
      ServiceCostRepository serviceCostRepository) {
    this.customerRepository = customerRepository;
    this.customerDeviceRepository = customerDeviceRepository;
    this.customerServiceRepository = customerServiceRepository;
    this.serviceCostRepository = serviceCostRepository;
  }

  @Override
  public List<Customer> getCustomers() {
    List<Customer> customers = customerRepository.findAll();

    return customers;
  }

  public Customer authenticateCustomerId(Authentication authentication, Long id) {
    String username = authentication.getName();

    Customer customer = customerRepository.getCustomerByIdAndUsername(id, username)
        .orElseThrow(() -> new CustomerNotFoundException(id));

    return customer;
  }

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
