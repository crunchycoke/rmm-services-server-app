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
   * Retrieves all customer devices and their information stored within the Customer Device table.
   *
   * @return A list of all Customer Device objects stored within the database.
   */
  List<CustomerDevice> getCustomerDevices();

  /**
   * Retrieves a list of Customer Device objects tied to the provided customer ID value.
   *
   * @param customerId Represents the customer ID tied to the device.
   * @return A list of customer devices attached to the customer ID provided.
   */
  List<CustomerDevice> getCustomerDevicesByCustomerId(Long customerId);

  /**
   * Retrieves the specific Customer Device object using the provided customer ID value.
   *
   * @param id         Represents the device ID to retrieve.
   * @param customerId Represents the customer ID tied to the device.
   * @return A specific customer device attached to the customer ID provided.
   */
  CustomerDevice getCustomerDeviceById(Long id, Long customerId);

  /**
   * Adds a new Customer Device object to the Customer Device table.
   *
   * @param newCustomerDevice Represents a new customer device object to be added.
   * @param customerId        Represents the customer ID to attach the device to.
   * @return The customer device that was added to the database.
   */
  CustomerDevice addCustomerDevice(CustomerDevice newCustomerDevice,
      Long customerId);

  /**
   * Updates the information on a specific customer device attached to a customer.
   *
   * @param id                Represents the customer device to be updated.
   * @param newCustomerDevice Represents the customer device information used for the update
   *                          process.
   * @param customerId        Represents the customer ID which the device will be updated.
   * @return The customer device that was updated with the updated information.
   */
  CustomerDevice updateCustomerDevice(Long id, CustomerDevice newCustomerDevice,
      Long customerId);

  /**
   * Deletes a Customer Device entry within the Customer Device table.
   *
   * @param id         Represents the customer device ID of the service to be deleted.
   * @param customerId Represents the customer ID to delete the device from.
   * @return An integer value noting how many records have been deleted.
   */
  int deleteCustomerDevice(Long id, Long customerId);
}
