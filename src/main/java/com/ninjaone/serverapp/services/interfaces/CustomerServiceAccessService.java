package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.models.CustomerService;
import java.util.List;

public interface CustomerServiceAccessService {

  public abstract List<CustomerService> getCustomerServices();

  public abstract List<CustomerService> getCustomerServicesByCustomerId(Long customerId);

  public abstract CustomerService getCustomerServiceById(Long id, Long customerId);

  public abstract CustomerService addCustomerService(CustomerService newCustomerService,
      Long customerId);

  public abstract int deleteCustomerService(ServiceType serviceType, Long customerId);

}
