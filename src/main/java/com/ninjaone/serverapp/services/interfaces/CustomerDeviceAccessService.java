package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.models.CustomerDevice;
import java.util.List;

/**
 * Service used for retrieving required data from the Customer Device table. Allows retrieval of all
 * customer devices, retrieval of devices by customer ID, device ID. Also allows adding, deleting,
 * and updating of customer devices.
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
