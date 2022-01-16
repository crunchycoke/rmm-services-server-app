package com.ninjaone.serverapp.services;

import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.exceptions.CustomerServiceNotFoundException;
import com.ninjaone.serverapp.exceptions.EntryCannotBeAddedException;
import com.ninjaone.serverapp.models.CustomerService;
import com.ninjaone.serverapp.repository.CustomerServiceRepository;
import com.ninjaone.serverapp.services.interfaces.CustomerServiceAccessService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class CustomerServiceRecordDetailsService implements CustomerServiceAccessService {

  private static final Logger log =
      LoggerFactory.getLogger(CustomerServiceRecordDetailsService.class);

  private final CustomerServiceRepository customerServiceRepository;

  /**
   *
   * @param customerServiceRepository
   */
  public CustomerServiceRecordDetailsService(CustomerServiceRepository customerServiceRepository) {
    this.customerServiceRepository = customerServiceRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CustomerService> getCustomerServices() {
    List<CustomerService> customerServices = customerServiceRepository.findAll();

    return customerServices;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CustomerService> getCustomerServicesByCustomerId(Long customerId) {
    List<CustomerService> customerServices =
        customerServiceRepository.getCustomerServicesByCustomerId(customerId);

    if (customerServices.isEmpty()) {
      throw new CustomerServiceNotFoundException(customerId);
    }

    return customerServices;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CustomerService getCustomerServiceById(Long id, Long customerId) {
    CustomerService customerService = customerServiceRepository
        .getCustomerServiceById(id, customerId)
        .orElseThrow(() -> new CustomerServiceNotFoundException(id));

    return customerService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CustomerService addCustomerService(CustomerService newCustomerService, Long customerId) {
    Optional<CustomerService> customerService =
        customerServiceRepository.getCustomerServiceByName(newCustomerService.getServiceType(),
            customerId);

    if (customerService.isPresent()) {
      throw new EntryCannotBeAddedException(newCustomerService);
    }

    int createdCustomerServices;
    try {
      createdCustomerServices = customerServiceRepository
          .insertNewCustomerService(newCustomerService.getServiceName(),
              newCustomerService.getServiceType().ordinal(),
              customerId);
    } catch (Exception ex) {
      throw new EntryCannotBeAddedException(newCustomerService, ex);
    }

    if (createdCustomerServices == 1) {
      Optional<CustomerService> updateCustomerService = customerServiceRepository
          .getCustomerServiceByName(newCustomerService.getServiceType(), customerId);

      return updateCustomerService.orElse(null);
    } else {
      throw new EntryCannotBeAddedException(newCustomerService);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int deleteCustomerService(ServiceType serviceType, Long customerId) {
    int deletedCustomerServices = customerServiceRepository.deleteByCustomerServiceType(serviceType,
        customerId);

    return deletedCustomerServices;
  }
}
