package com.ninjaone.serverapp.repository;

import com.ninjaone.serverapp.models.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository used for accessing data within the Customer table.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  // Get specific customer
  @Query("select customer " +
      "from Customer customer " +
      "where customer.username = :username")
  Optional<Customer> getCustomerByUsername(@Param("username") String username);

  // Get specific customer
  @Query("select customer " +
      "from Customer customer " +
      "where customer.id = :id")
  Optional<Customer> getCustomerById(@Param("id") Long id);

  // Get specific customer
  @Query("select customer " +
      "from Customer customer " +
      "where customer.id = :id and customer.username = :username")
  Optional<Customer> getCustomerByIdAndUsername(@Param("id") Long id,
      @Param("username") String username);
}
