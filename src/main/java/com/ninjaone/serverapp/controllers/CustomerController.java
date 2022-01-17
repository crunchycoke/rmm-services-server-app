package com.ninjaone.serverapp.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ninjaone.serverapp.modelassemblers.CustomerModelAssembler;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.models.CustomerBill;
import com.ninjaone.serverapp.services.CustomerRecordDetailsService;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller used for accessing data related to the customer database.
 */
@RestController
public class CustomerController {

  private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

  private final CustomerRecordDetailsService customerRecordDetailsService;
  private final CustomerModelAssembler customerAssembler;

  /**
   * Constructs the customer controller class.
   *
   * @param customerRecordDetailsService Represents the customer service loaded through dependency
   *                                     injection.
   * @param customerAssembler            Represents the customer assembler loaded through dependency
   *                                     injection.
   */
  public CustomerController(CustomerRecordDetailsService customerRecordDetailsService,
      CustomerModelAssembler customerAssembler) {
    this.customerRecordDetailsService = customerRecordDetailsService;
    this.customerAssembler = customerAssembler;
  }

  /**
   * Retrieves all customers available.
   *
   * @return A list of customer objects.
   */
  @GetMapping("/customers")
  public CollectionModel<EntityModel<Customer>> getAllCustomers() {
    log.info("Attempting to get all customers");

    List<EntityModel<Customer>> customers = customerRecordDetailsService.getCustomers()
        .stream()
        .map(customerAssembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(customers,
        linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel());
  }

  /**
   * Retrieves a customer based off the ID provided.
   *
   * @param authentication Represents the authentication token provided within the header.
   * @param id             Represents the customer ID to retrieve.
   * @return A customer object.
   */
  @GetMapping("/customers/{id}")
  public EntityModel<Customer> getCustomerById(Authentication authentication,
      @PathVariable Long id) {
    log.info("Attempting to get customer test " + id);

    Customer customer = customerRecordDetailsService
        .authenticateCustomerId(authentication, id);

    return customerAssembler.toModel(customer);
  }

  /**
   * Retrievesthe customer billing by the ID provided.
   *
   * @param authentication Represents the authentication token provided within the header.
   * @param id             Represents the customer ID to get the billing information for.
   * @return A customer bill object with the data containing the costs of services and devices.
   */
  @GetMapping("/customers/{id}/billing")
  public CustomerBill getCustomerBillingById(Authentication authentication,
      @PathVariable Long id) {
    log.info("Attempting to get customer " + id + " billing info");

    customerRecordDetailsService.authenticateCustomerId(authentication, id);

    CustomerBill customerBill = customerRecordDetailsService.calculateCustomerBill(id);

    log.info("Finished calculating customer " + id + " billing info");

    return customerBill;
  }
}
