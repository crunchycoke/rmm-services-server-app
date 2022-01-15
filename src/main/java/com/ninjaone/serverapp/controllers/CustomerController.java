package com.ninjaone.serverapp.controllers;

import com.ninjaone.serverapp.exceptions.CustomerNotFoundException;
import com.ninjaone.serverapp.exceptions.EntryCannotBeAddedException;
import com.ninjaone.serverapp.modelassemblers.CustomerModelAssembler;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerRepository customerRepository;
    private final CustomerModelAssembler customerAssembler;

    public CustomerController(CustomerRepository customerRepository,
                              CustomerModelAssembler customerAssembler) {
        this.customerRepository = customerRepository;
        this.customerAssembler = customerAssembler;
    }

    @GetMapping("/customers")
    public CollectionModel<EntityModel<Customer>> getAllCustomers() {
        log.info("Attempting to get all customers.");

        List<EntityModel<Customer>> customers = customerRepository.findAll().stream()
                .map(customerAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(customers,
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel());
    }

    @GetMapping("/customers/{id}")
    public EntityModel<Customer> getCustomerById(@PathVariable Long id) {
        log.info("Attempting to get customer " + id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        return customerAssembler.toModel(customer);
    }

    @PostMapping("/customers")
    public ResponseEntity<?> addCustomer(@RequestBody Customer newCustomer) {
        log.info("Attempting to add customer " + newCustomer);

        try {
            EntityModel<Customer> customerEntityModel =
                    customerAssembler.toModel(customerRepository.save(newCustomer));

            return ResponseEntity.created(customerEntityModel
                            .getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(customerEntityModel);
        } catch (Exception ex) {
            throw new EntryCannotBeAddedException(newCustomer, ex);
        }
    }
}
