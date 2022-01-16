package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.models.Customer;
import java.util.List;

/**
 *
 */
public interface CustomerAccessService {

  /**
   * @return
   */
  public abstract List<Customer> getCustomers();
}
