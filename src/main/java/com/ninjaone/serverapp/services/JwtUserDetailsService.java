package com.ninjaone.serverapp.services;

import com.ninjaone.serverapp.exceptions.EntryCannotBeAddedException;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.repository.CustomerRepository;
import java.util.ArrayList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder bcryptEncoder;

  /**
   * Constructs the service used for loading and saving the user credentials for authentication
   * purposes.
   *
   * @param customerRepository Represents the repository used for accessing the customer table
   *                           loaded through dependency injection.
   * @param bcryptEncoder      Represents the encryption encoder used for encrypting the customer
   *                           user data loaded through dependency injection.
   */
  public JwtUserDetailsService(CustomerRepository customerRepository,
      PasswordEncoder bcryptEncoder) {
    this.customerRepository = customerRepository;
    this.bcryptEncoder = bcryptEncoder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Customer customer = customerRepository.getCustomerByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("User not found with username: " + username));

    return new org.springframework.security.core.userdetails.User(customer.getUsername(),
        customer.getPassword(),
        new ArrayList<>());
  }

  /**
   * Saves the customer information within the customer table while also encoding the customer's
   * password.
   *
   * @param customer Represents the customer being saved within the database.
   * @return A customer object saved.
   */
  public Customer save(Customer customer) {
    customer.setPassword(bcryptEncoder.encode(customer.getPassword()));

    try {
      return customerRepository.save(customer);
    } catch (Exception ex) {
      throw new EntryCannotBeAddedException(customer, ex);
    }
  }
}
