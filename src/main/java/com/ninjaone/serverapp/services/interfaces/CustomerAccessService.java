package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.models.Customer;
import java.util.List;

public interface CustomerAccessService {

  public abstract List<Customer> getCustomers();
}
