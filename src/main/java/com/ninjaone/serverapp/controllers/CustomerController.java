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
 *
 */
@RestController
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerRecordDetailsService customerRecordDetailsService;
    private final CustomerModelAssembler customerAssembler;

    /**
     *
     * @param customerRecordDetailsService
     * @param customerAssembler
     */
    public CustomerController(CustomerRecordDetailsService customerRecordDetailsService,
        CustomerModelAssembler customerAssembler) {
        this.customerRecordDetailsService = customerRecordDetailsService;
        this.customerAssembler = customerAssembler;
    }

    /**
     *
     * @return
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
     *
     * @param authentication
     * @param id
     * @return
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
     *
     * @param authentication
     * @param id
     * @return
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
