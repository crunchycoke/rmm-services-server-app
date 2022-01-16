package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.models.CustomerDevice;
import java.util.List;

/**
 *
 */
public interface CustomerDeviceAccessService {

  /**
   * @return
   */
  public abstract List<CustomerDevice> getCustomerDevices();

  /**
   * @param customerId
   * @return
   */
  public abstract List<CustomerDevice> getCustomerDevicesByCustomerId(Long customerId);

  /**
   * @param id
   * @param customerId
   * @return
   */
  public abstract CustomerDevice getCustomerDeviceById(Long id, Long customerId);

  /**
   * @param newCustomerDevice
   * @param customerId
   * @return
   */
  public abstract CustomerDevice addCustomerDevice(CustomerDevice newCustomerDevice,
      Long customerId);

  /**
   * @param id
   * @param newCustomerDevice
   * @param customerId
   * @return
   */
  public abstract CustomerDevice updateCustomerDevice(Long id, CustomerDevice newCustomerDevice,
      Long customerId);

  /**
   * @param id
   * @param customerId
   * @return
   */
  public abstract int deleteCustomerDevice(Long id, Long customerId);
}
