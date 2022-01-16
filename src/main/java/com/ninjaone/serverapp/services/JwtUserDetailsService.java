package com.ninjaone.serverapp.services;

import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.repository.CustomerRepository;
import java.util.ArrayList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder bcryptEncoder;

  public JwtUserDetailsService(CustomerRepository customerRepository,
      PasswordEncoder bcryptEncoder) {
    this.customerRepository = customerRepository;
    this.bcryptEncoder = bcryptEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Customer customer = customerRepository.getCustomerByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("User not found with username: " + username));

    return new org.springframework.security.core.userdetails.User(customer.getUsername(),
        customer.getPassword(),
        new ArrayList<>());
  }

  public Customer save(Customer customer) {
    customer.setPassword(bcryptEncoder.encode(customer.getPassword()));

    return customerRepository.save(customer);
  }
}