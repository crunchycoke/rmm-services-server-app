package com.ninjaone.serverapp.services;

import com.ninjaone.serverapp.exceptions.CustomerNotFoundException;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.repository.CustomerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CustomerAccessService {

  private final CustomerRepository customerRepository;

  public CustomerAccessService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public Customer authenticateCustomerId(Authentication authentication, Long id) {
    String username = authentication.getName();

    Customer customer = customerRepository.getCustomerByIdAndUsername(id, username)
        .orElseThrow(() -> new CustomerNotFoundException(id));

    return customer;
  }
}
