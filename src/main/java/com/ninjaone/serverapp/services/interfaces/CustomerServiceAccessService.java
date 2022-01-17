package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.models.CustomerService;
import java.util.List;

/**
 * Service used for retrieving required data from the Customer Service table. Allows retrieval of
 * all customer services, retrieval of services by customer ID, services ID. Also allows adding,
 * deleting, and updating of customer services.
 */
public interface CustomerServiceAccessService {

  /**
   * Retrieves all customer services and their information stored within the Customer Service
   * table.
   *
   * @return A list of all Customer Service objects stored within the database.
   */
  List<CustomerService> getCustomerServices();

  /**
   * Retrieves a list of Customer Service objects tied to the provided customer ID value.
   *
   * @param customerId Represents the customer ID tied to the service.
   * @return A list of customer services attached to the customer ID provided.
   */
  List<CustomerService> getCustomerServicesByCustomerId(Long customerId);

  /**
   * Retrieves the specific Customer Service object using the provided customer ID value.
   *
   * @param id         Represents the service ID to retrieve.
   * @param customerId Represents the customer ID tied to the service.
   * @return A specific customer service attached to the customer ID provided.
   */
  CustomerService getCustomerServiceById(Long id, Long customerId);

  /**
   * Adds a new Customer Service object within the Customer Service table.
   *
   * @param newCustomerService Represents a new customer service object to be added.
   * @param customerId         Represents the customer ID to attach the service to.
   * @return Adds a new Customer Service object to the Customer Service table.
   * @return The customer service that was added to the database.
   */
  CustomerService addCustomerService(CustomerService newCustomerService,
      Long customerId);

  /**
   * Deletes a Customer Service entry within the Customer Service table.
   *
   * @param serviceType Represents the service type of the service to be deleted.
   * @param customerId  Represents the customer ID to delete the service from.
   * @return An integer value noting how many records have been deleted.
   */
  int deleteCustomerService(ServiceType serviceType, Long customerId);

}
