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
   * Constructs the service used for accessing the records and details within the service table.
   *
   * @param customerServiceRepository Represents the repository used for accessing the service table
   *                                  loaded through dependency injection.
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

    log.info("Retrieved all available customer services.");

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
      log.info("No customer services found under customer ID" + customerId + ".");

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

    log.info("Retrieved customer service using service ID "
        + id + " under customer " + customerId + ".");

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
      log.info("Unable to add customer service to table.", ex);

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

    log.info("Deleted " + deletedCustomerServices + " services.");

    return deletedCustomerServices;
  }
}
