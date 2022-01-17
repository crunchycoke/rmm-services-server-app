package com.ninjaone.serverapp.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.modelassemblers.CustomerServiceModelAssembler;
import com.ninjaone.serverapp.models.CustomerService;
import com.ninjaone.serverapp.services.CustomerRecordDetailsService;
import com.ninjaone.serverapp.services.CustomerServiceRecordDetailsService;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used for accessing data related to the customer service database.
 */
@RestController
public class CustomerServiceController {

  private static final Logger log = LoggerFactory.getLogger(CustomerServiceController.class);

  private final CustomerServiceRecordDetailsService customerServiceRecordDetailsService;
  private final CustomerRecordDetailsService customerRecordDetailsService;
  private final CustomerServiceModelAssembler customerServiceAssembler;

  /**
   * Constructs the customer service controller class.
   *
   * @param customerServiceRecordDetailsService Represents the customer service loaded through
   *                                            dependency injection.
   * @param customerRecordDetailsService        Represents the customer loaded through dependency
   *                                            injection.
   * @param customerServiceAssembler            Represents the customer service assembler loaded
   *                                            through dependency injection.
   */
  public CustomerServiceController(
      CustomerServiceRecordDetailsService customerServiceRecordDetailsService,
      CustomerRecordDetailsService customerRecordDetailsService,
      CustomerServiceModelAssembler customerServiceAssembler) {
    this.customerServiceRecordDetailsService = customerServiceRecordDetailsService;
    this.customerRecordDetailsService = customerRecordDetailsService;
    this.customerServiceAssembler = customerServiceAssembler;
  }

  /**
   * Retrieves all customer services within the database.
   *
   * @return A list of all customer services available.
   */
  @GetMapping("/customers/services")
  public CollectionModel<EntityModel<CustomerService>> getAllCustomerServices() {
    log.info("Attempting to get all customer services.");

    List<EntityModel<CustomerService>> customerServices = customerServiceRecordDetailsService.getCustomerServices()
        .stream()
        .map(customerServiceAssembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(customerServices, linkTo(methodOn(CustomerServiceController.class)
        .getAllCustomerServices()).withSelfRel());
  }

  /**
   * Retrieves all customer services attached to a customer.
   *
   * @param authentication Represents the authentication token provided within the header.
   * @param customerId     Represents the customer ID where the service is attached.
   * @return A list of all services under a customer.
   */
  @GetMapping("/customers/{customerId}/services")
  public CollectionModel<EntityModel<CustomerService>> getCustomerServiceByCustomerId(
      Authentication authentication,
      @PathVariable Long customerId) {
    log.info("Attempting to get all customer " + customerId + " services.");

    customerRecordDetailsService.authenticateCustomerId(authentication, customerId);

    List<EntityModel<CustomerService>> customerServices =
        customerServiceRecordDetailsService.getCustomerServicesByCustomerId(customerId)
            .stream()
            .map(customerServiceAssembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(customerServices, linkTo(methodOn(CustomerServiceController.class)
        .getAllCustomerServices()).withSelfRel());
  }

  /**
   * Retrieves the customer service from a specific customer by the service ID.
   *
   * @param authentication Represents the authentication token provided within the header.
   * @param customerId     Represents the customer ID where the service is attached.
   * @param id             Represents the customer service ID to be pulled from the database.
   * @return The customer service object retrieved.
   */
  @GetMapping("/customers/{customerId}/services/{id}")
  public EntityModel<CustomerService> getCustomerServiceById(Authentication authentication,
      @PathVariable Long customerId,
      @PathVariable Long id) {
    log.info("Attempting to get customer " + customerId + " service " + id);

    customerRecordDetailsService.authenticateCustomerId(authentication, customerId);

    CustomerService customerService =
        customerServiceRecordDetailsService.getCustomerServiceById(id, customerId);

    return customerServiceAssembler.toModel(customerService);
  }

  /**
   * Adds a new customer service object to the database attached to a specific customer ID.
   *
   * @param authentication     Represents the authentication token provided within the header.
   * @param newCustomerService Represents the customer service to be added.
   * @param customerId         Represents the customer ID where the service will be attached.
   * @return The added customer service object.
   */
  @PostMapping("/customers/{customerId}/services")
  public ResponseEntity<?> addCustomerService(Authentication authentication,
      @RequestBody CustomerService newCustomerService,
      @PathVariable Long customerId) {
    log.info("Attempting to add customer service " + newCustomerService);

    customerRecordDetailsService.authenticateCustomerId(authentication, customerId);

    CustomerService customerService =
        customerServiceRecordDetailsService.addCustomerService(newCustomerService, customerId);

    EntityModel<CustomerService> customerServiceEntityModel = customerServiceAssembler.toModel(
        customerService);

    return ResponseEntity.created(customerServiceEntityModel
            .getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(customerServiceEntityModel);
  }

  /**
   * Deletes a specific service under a customer.
   *
   * @param authentication Represents the authentication token provided within the header.
   * @param customerId     Represents the customer ID where the service is attached.
   * @param serviceType    Represents the service type to be removed.
   * @return A success or fail response.
   */
  @DeleteMapping("/customers/{customerId}/services/{serviceType}")
  public ResponseEntity<?> deleteCustomerService(Authentication authentication,
      @PathVariable Long customerId,
      @PathVariable ServiceType serviceType) {
    log.info("Attempting to delete customer " + customerId + " service " + serviceType);

    customerRecordDetailsService.authenticateCustomerId(authentication, customerId);

    int deletedCustomerServices =
        customerServiceRecordDetailsService.deleteCustomerService(serviceType, customerId);

    if (deletedCustomerServices == 1) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }
}
