package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.models.CustomerDevice;
import java.util.List;

public interface CustomerDeviceAccessService {

  public abstract List<CustomerDevice> getCustomerDevices();

  public abstract List<CustomerDevice> getCustomerDevicesByCustomerId(Long customerId);

  public abstract CustomerDevice getCustomerDeviceById(Long id, Long customerId);

  public abstract CustomerDevice addCustomerDevice(CustomerDevice newCustomerDevice,
      Long customerId);

  public abstract CustomerDevice updateCustomerDevice(Long id, CustomerDevice newCustomerDevice,
      Long customerId);

  public abstract int deleteCustomerDevice(Long id, Long customerId);
}
