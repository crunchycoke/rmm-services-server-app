package com.ninjaone.serverapp.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ninjaone.serverapp.modelassemblers.CustomerDeviceModelAssembler;
import com.ninjaone.serverapp.models.CustomerDevice;
import com.ninjaone.serverapp.services.CustomerDeviceRecordDetailsService;
import com.ninjaone.serverapp.services.CustomerRecordDetailsService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used for accessing data related to the customer device database.
 */
@RestController
public class CustomerDeviceController {

  private static final Logger log = LoggerFactory.getLogger(CustomerDeviceController.class);

  private final CustomerDeviceRecordDetailsService customerDeviceRecordDetailsService;
  private final CustomerRecordDetailsService customerRecordDetailsService;
  private final CustomerDeviceModelAssembler customerDeviceAssembler;

  /**
   * Constructs the customer device controller class.
   *
   * @param customerDeviceRecordDetailsService Represents the customer device loaded through
   *                                           dependency injection.
   * @param customerRecordDetailsService       Represents the customer loaded through dependency
   *                                           injection.
   * @param customerDeviceAssembler            Represents the customer device assembler loaded
   *                                           through dependency injection.
   */
  public CustomerDeviceController(
      CustomerDeviceRecordDetailsService customerDeviceRecordDetailsService,
      CustomerRecordDetailsService customerRecordDetailsService,
      CustomerDeviceModelAssembler customerDeviceAssembler) {
    this.customerDeviceRecordDetailsService = customerDeviceRecordDetailsService;
    this.customerRecordDetailsService = customerRecordDetailsService;
    this.customerDeviceAssembler = customerDeviceAssembler;
  }

  /**
   * Retrieves all customer devices.
   *
   * @return A list of all customer devices.
   */
  @GetMapping("/customers/devices")
  public CollectionModel<EntityModel<CustomerDevice>> getAllCustomerDevices() {
    log.info("Attempting to get all customer devices.");

    List<EntityModel<CustomerDevice>> customerDevices =
        customerDeviceRecordDetailsService.getCustomerDevices()
            .stream()
            .map(customerDeviceAssembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(customerDevices, linkTo(methodOn(CustomerDeviceController.class)
        .getAllCustomerDevices()).withSelfRel());
  }

  /**
   * Retrieves all devices attached to a customer
   *
   * @param authentication Represents the authentication token provided within the header.
   * @param customerId     Represents the customer ID where the device is attached.
   * @return A list of customer devices attached to the customer.
   */
  @GetMapping("/customers/{customerId}/devices")
  public CollectionModel<EntityModel<CustomerDevice>> getCustomerDeviceByCustomerId(
      Authentication authentication,
      @PathVariable Long customerId) {
    log.info("Attempting to get all customer " + customerId + " devices.");

    customerRecordDetailsService.authenticateCustomerId(authentication, customerId);

    List<EntityModel<CustomerDevice>> customerDevices =
        customerDeviceRecordDetailsService.getCustomerDevicesByCustomerId(customerId)
            .stream()
            .map(customerDeviceAssembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(customerDevices, linkTo(methodOn(CustomerDeviceController.class)
        .getAllCustomerDevices()).withSelfRel());
  }

  /**
   * Retrieves a customer device by the customer ID provided.
   *
   * @param authentication Represents the authentication token provided within the header.
   * @param customerId     Represents the customer ID where the device is attached.
   * @param id             Represents the customer device attached to the customer
   * @return A specific customer device attached to the customer
   */
  @GetMapping("/customers/{customerId}/devices/{id}")
  public EntityModel<CustomerDevice> getCustomerDeviceById(Authentication authentication,
      @PathVariable Long customerId,
      @PathVariable Long id) {
    log.info("Attempting to get customer " + customerId + " device " + id);

    customerRecordDetailsService.authenticateCustomerId(authentication, customerId);

    CustomerDevice customerDevice =
        customerDeviceRecordDetailsService.getCustomerDeviceById(id, customerId);

    return customerDeviceAssembler.toModel(customerDevice);
  }

  /**
   * Adds a new customer device to an existing customer.
   *
   * @param authentication    Represents the authentication token provided within the header.
   * @param newCustomerDevice Represents the customer device to be added.
   * @param customerId        Represents the customer ID where the device should be attached.
   * @return The newly added device.
   */
  @PostMapping("/customers/{customerId}/devices")
  public ResponseEntity<?> addCustomerDevice(Authentication authentication,
      @RequestBody CustomerDevice newCustomerDevice,
      @PathVariable Long customerId) {
    log.info("Attempting to add customer device " + newCustomerDevice);

    customerRecordDetailsService.authenticateCustomerId(authentication, customerId);

    CustomerDevice customerDevice =
        customerDeviceRecordDetailsService.addCustomerDevice(newCustomerDevice, customerId);

    EntityModel<CustomerDevice> customerDeviceEntityModel = customerDeviceAssembler.toModel(
        customerDevice);

    return ResponseEntity.created(customerDeviceEntityModel
            .getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(customerDeviceEntityModel);
  }

  /**
   * Upserts a device to an existing customer.
   *
   * @param authentication    Represents the authentication token provided within the header.
   * @param newCustomerDevice Represents the new customer device to upsert.
   * @param customerId        Represents the customer ID where the device is attached.
   * @param id                Represents the ID of the device to update or add.
   * @return The updated device object.
   */
  @PutMapping("/customers/{customerId}/devices/{id}")
  public ResponseEntity<?> updateCustomerDevice(Authentication authentication,
      @RequestBody CustomerDevice newCustomerDevice,
      @PathVariable Long customerId,
      @PathVariable Long id) {
    log.info("Attempting to update customer device " + newCustomerDevice);

    customerRecordDetailsService.authenticateCustomerId(authentication, customerId);

    CustomerDevice updatedCustomerDevice = customerDeviceRecordDetailsService.updateCustomerDevice(
        id, newCustomerDevice, customerId);

    EntityModel<CustomerDevice> customerDeviceEntityModel = customerDeviceAssembler.toModel(
        updatedCustomerDevice);

    return ResponseEntity.created(customerDeviceEntityModel
        .getRequiredLink(IanaLinkRelations.SELF)
        .toUri()).body(customerDeviceEntityModel);
  }

  /**
   * Deletes a specific device under a customer.
   *
   * @param authentication Represents the authentication token provided within the header.
   * @param customerId     Represents the customer ID where the device is attached.
   * @param id             Represents the device ID to delete.
   * @return A success or fail response.
   */
  @DeleteMapping("/customers/{customerId}/devices/{id}")
  public ResponseEntity<?> deleteCustomerDevice(Authentication authentication,
      @PathVariable Long customerId,
      @PathVariable Long id) {
    log.info("Attempting to delete customer " + customerId + " device " + id);

    customerRecordDetailsService.authenticateCustomerId(authentication, customerId);

    int deletedCustomerDevices =
        customerDeviceRecordDetailsService.deleteCustomerDevice(id, customerId);

    if (deletedCustomerDevices == 1) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }
}
