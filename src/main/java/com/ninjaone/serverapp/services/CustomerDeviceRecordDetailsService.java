package com.ninjaone.serverapp.services;

import com.ninjaone.serverapp.exceptions.CustomerDeviceNotFoundException;
import com.ninjaone.serverapp.exceptions.CustomerNotFoundException;
import com.ninjaone.serverapp.exceptions.EntryCannotBeAddedException;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.models.CustomerDevice;
import com.ninjaone.serverapp.repository.CustomerDeviceRepository;
import com.ninjaone.serverapp.repository.CustomerRepository;
import com.ninjaone.serverapp.services.interfaces.CustomerDeviceAccessService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CustomerDeviceRecordDetailsService implements CustomerDeviceAccessService {

  private static final Logger log =
      LoggerFactory.getLogger(CustomerDeviceRecordDetailsService.class);

  private final CustomerRepository customerRepository;
  private final CustomerDeviceRepository customerDeviceRepository;

  /**
   *
   * @param customerRepository
   * @param customerDeviceRepository
   */
  public CustomerDeviceRecordDetailsService(CustomerRepository customerRepository,
      CustomerDeviceRepository customerDeviceRepository) {
    this.customerRepository = customerRepository;
    this.customerDeviceRepository = customerDeviceRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CustomerDevice> getCustomerDevices() {
    List<CustomerDevice> customerDevices = customerDeviceRepository.findAll();

    return customerDevices;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CustomerDevice> getCustomerDevicesByCustomerId(Long customerId) {
    List<CustomerDevice> customerDevices =
        customerDeviceRepository.getCustomerDevicesByCustomerId(customerId);

    if (customerDevices.isEmpty()) {
      throw new CustomerDeviceNotFoundException(customerId);
    }

    return customerDevices;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CustomerDevice getCustomerDeviceById(Long id, Long customerId) {
    CustomerDevice customerDevice = customerDeviceRepository.getCustomerDeviceById(id, customerId)
        .orElseThrow(() -> new CustomerDeviceNotFoundException(id));

    return customerDevice;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CustomerDevice addCustomerDevice(CustomerDevice newCustomerDevice, Long customerId) {
    Optional<CustomerDevice> customerDevice = customerDeviceRepository.getCustomerDeviceById(
        newCustomerDevice.getId(), customerId);

    if (customerDevice.isPresent()) {
      throw new EntryCannotBeAddedException(newCustomerDevice);
    }

    int createdCustomerServices;
    try {
      createdCustomerServices = customerDeviceRepository
          .insertNewCustomerDevice(newCustomerDevice.getId(),
              newCustomerDevice.getSystemName(),
              newCustomerDevice.getDeviceType().ordinal(),
              customerId);
    } catch (Exception ex) {
      throw new EntryCannotBeAddedException(newCustomerDevice, ex);
    }

    if (createdCustomerServices == 1) {
      Optional<CustomerDevice> updateCustomerDevice = customerDeviceRepository
          .getCustomerDeviceById(newCustomerDevice.getId(), customerId);

      return updateCustomerDevice.orElse(null);
    } else {
      throw new EntryCannotBeAddedException(newCustomerDevice);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CustomerDevice updateCustomerDevice(Long id, CustomerDevice newCustomerDevice,
      Long customerId) {
    try {
      Customer customer = customerRepository.findById(customerId)
          .orElseThrow(() -> new CustomerNotFoundException(customerId));

      newCustomerDevice.setCustomer(customer);

      CustomerDevice updatedCustomerDevice =
          customerDeviceRepository.getCustomerDeviceById(id, customerId)
              .map(customerDevice -> {
                customerDevice.setDeviceType(newCustomerDevice.getDeviceType());
                customerDevice.setSystemName(newCustomerDevice.getSystemName());

                log.info("Attempting to update customer device.");

                return customerDeviceRepository.save(customerDevice);
              })
              .orElseGet(() -> {
                newCustomerDevice.setId(id);

                log.info("Attempting to added customer device.");

                return customerDeviceRepository.save(newCustomerDevice);
              });
      return updatedCustomerDevice;
    } catch (Exception ex) {
      throw new EntryCannotBeAddedException(newCustomerDevice, ex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int deleteCustomerDevice(Long id, Long customerId) {
    int deletedCustomerDevices = customerDeviceRepository.deleteByCustomerDeviceId(id, customerId);

    return deletedCustomerDevices;
  }
}
