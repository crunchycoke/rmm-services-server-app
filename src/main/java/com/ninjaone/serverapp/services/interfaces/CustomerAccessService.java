package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.models.Customer;
import java.util.List;

/**
 * Service used for retrieving required data from the Customer table. Main functionality is
 * retrieving all customers registered.
 */
public interface CustomerAccessService {

  /**
   * Retrieves a list of customer objects from the Customer database table.
   *
   * @return A list of customer objects.
   */
  public abstract List<Customer> getCustomers();
}
