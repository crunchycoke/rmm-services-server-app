package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.models.Customer;
import java.util.List;

/**
 * Service used for retrieving required data from the Customer table. Main functionality is
 * retrieving all customers registered.
 */
public interface CustomerAccessService {

  /**
   * Retrieves all customers and their information stored within the Customer table.
   *
   * @return A list of all Customer objects stored within the database.
   */
  List<Customer> getCustomers();
}
