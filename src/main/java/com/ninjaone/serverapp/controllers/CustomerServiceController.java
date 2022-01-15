package com.ninjaone.serverapp.controllers;

import com.ninjaone.serverapp.exceptions.CustomerServiceNotFoundException;
import com.ninjaone.serverapp.modelassemblers.CustomerServiceModelAssembler;
import com.ninjaone.serverapp.models.CustomerService;
import com.ninjaone.serverapp.repository.CustomerServiceRepository;
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
public class CustomerServiceController {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceController.class);

    private final CustomerServiceRepository customerServiceRepository;
    private final CustomerServiceModelAssembler customerServiceAssembler;

    public CustomerServiceController(CustomerServiceRepository customerServiceRepository,
                                     CustomerServiceModelAssembler customerServiceAssembler) {
        this.customerServiceRepository = customerServiceRepository;
        this.customerServiceAssembler = customerServiceAssembler;
    }

    @GetMapping("/customers/services")
    public CollectionModel<EntityModel<CustomerService>> getAllCustomerServices() {
        log.info("Attempting to get all customer services.");

        List<EntityModel<CustomerService>> customerServices =
                customerServiceRepository.findAll().stream()
                        .map(customerServiceAssembler::toModel)
                        .collect(Collectors.toList());

        return CollectionModel.of(customerServices, linkTo(methodOn(CustomerServiceController.class)
                .getAllCustomerServices()).withSelfRel());
    }

    @GetMapping("/customers/services/{id}")
    public EntityModel<CustomerService> getCustomerServicesById(@PathVariable Long id) {
        log.info("Attempting to get customer service " + id);

        CustomerService customerService = customerServiceRepository.findById(id)
                .orElseThrow(() -> new CustomerServiceNotFoundException(id));

        return customerServiceAssembler.toModel(customerService);
    }

    @PostMapping("/customers/{customerId}/services")
    public ResponseEntity<?> addCustomerService(@RequestBody CustomerService newCustomerService,
                                                @PathVariable Long customerId) {
        log.info("Attempting to add customer service " + newCustomerService);

        EntityModel<CustomerService> customerServiceEntityModel =
                customerServiceAssembler.toModel(customerServiceRepository.save(newCustomerService));

        return ResponseEntity.created(customerServiceEntityModel
                        .getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(customerServiceEntityModel);
    }

    @DeleteMapping("/customers/{customerId}/services/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long customerId,
                                            @PathVariable Long id) {
        customerServiceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
