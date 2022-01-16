package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.models.CustomerService;
import java.util.List;

/**
 *
 */
public interface CustomerServiceAccessService {

  /**
   * @return
   */
  public abstract List<CustomerService> getCustomerServices();

  /**
   * @param customerId
   * @return
   */
  public abstract List<CustomerService> getCustomerServicesByCustomerId(Long customerId);

  /**
   * @param id
   * @param customerId
   * @return
   */
  public abstract CustomerService getCustomerServiceById(Long id, Long customerId);

  /**
   * @param newCustomerService
   * @param customerId
   * @return
   */
  public abstract CustomerService addCustomerService(CustomerService newCustomerService,
      Long customerId);

  /**
   * @param serviceType
   * @param customerId
   * @return
   */
  public abstract int deleteCustomerService(ServiceType serviceType, Long customerId);

}
