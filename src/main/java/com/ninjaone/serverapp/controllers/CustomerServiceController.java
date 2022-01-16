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

@RestController
public class CustomerServiceController {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceController.class);

    private final CustomerServiceRecordDetailsService customerServiceRecordDetailsService;
    private final CustomerRecordDetailsService customerRecordDetailsService;
    private final CustomerServiceModelAssembler customerServiceAssembler;

    public CustomerServiceController(
        CustomerServiceRecordDetailsService customerServiceRecordDetailsService,
        CustomerRecordDetailsService customerRecordDetailsService,
        CustomerServiceModelAssembler customerServiceAssembler) {
        this.customerServiceRecordDetailsService = customerServiceRecordDetailsService;
        this.customerRecordDetailsService = customerRecordDetailsService;
        this.customerServiceAssembler = customerServiceAssembler;
    }

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
