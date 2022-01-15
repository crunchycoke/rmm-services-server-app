package com.ninjaone.serverapp.controllers;

import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.exceptions.CustomerServiceNotFoundException;
import com.ninjaone.serverapp.exceptions.EntryCannotBeAddedException;
import com.ninjaone.serverapp.modelassemblers.CustomerServiceModelAssembler;
import com.ninjaone.serverapp.models.CustomerService;
import com.ninjaone.serverapp.repository.CustomerRepository;
import com.ninjaone.serverapp.repository.CustomerServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CustomerServiceController {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceController.class);

    private final CustomerRepository customerRepository;
    private final CustomerServiceRepository customerServiceRepository;
    private final CustomerServiceModelAssembler customerServiceAssembler;

    public CustomerServiceController(CustomerRepository customerRepository,
                                     CustomerServiceRepository customerServiceRepository,
                                     CustomerServiceModelAssembler customerServiceAssembler) {
        this.customerRepository = customerRepository;
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

    @GetMapping("/customers/{customerId}/services")
    public CollectionModel<EntityModel<CustomerService>> getCustomerServiceByCustomerId(@PathVariable Long customerId) {
        log.info("Attempting to get all customer " + customerId + " services.");

        List<EntityModel<CustomerService>> customerServices =
                customerServiceRepository.getCustomerServicesByCustomerId(customerId).stream()
                        .map(customerServiceAssembler::toModel)
                        .collect(Collectors.toList());

        if (customerServices.isEmpty()) {
            throw new CustomerServiceNotFoundException(customerId);
        }

        return CollectionModel.of(customerServices, linkTo(methodOn(CustomerServiceController.class)
                .getAllCustomerServices()).withSelfRel());
    }

    @GetMapping("/customers/{customerId}/services/{id}")
    public EntityModel<CustomerService> getCustomerServiceById(@PathVariable Long customerId,
                                                               @PathVariable Long id) {
        log.info("Attempting to get customer " + customerId + " service " + id);

        CustomerService customerService = customerServiceRepository.getCustomerServiceById(id, customerId)
                .orElseThrow(() -> new CustomerServiceNotFoundException(id));

        return customerServiceAssembler.toModel(customerService);
    }

    @PostMapping("/customers/{customerId}/services")
    public ResponseEntity<?> addCustomerService(@RequestBody CustomerService newCustomerService,
                                                @PathVariable Long customerId) {
        log.info("Attempting to add customer service " + newCustomerService);

        Optional<CustomerService> customerService =
                customerServiceRepository.getCustomerServiceByName(newCustomerService.getServiceType(), customerId);

        if (customerService.isPresent()) {
            throw new EntryCannotBeAddedException(newCustomerService);
        } else {
            var createdCustomerServices = customerServiceRepository
                    .insertNewCustomerService(newCustomerService.getServiceName(), newCustomerService.getServiceType().ordinal(), customerId);

            if (createdCustomerServices == 1) {
                Optional<CustomerService> updateCustomerService = customerServiceRepository
                        .getCustomerServiceByName(newCustomerService.getServiceType(), customerId);

                if (updateCustomerService.isPresent()) {
                    EntityModel<CustomerService> customerServiceEntityModel = customerServiceAssembler.toModel(updateCustomerService.get());

                    return ResponseEntity.created(customerServiceEntityModel
                                    .getRequiredLink(IanaLinkRelations.SELF).toUri())
                            .body(customerServiceEntityModel);
                } else {
                    return ResponseEntity.unprocessableEntity().build();
                }
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @DeleteMapping("/customers/{customerId}/services/{serviceType}")
    public ResponseEntity<?> deleteCustomerService(@PathVariable Long customerId,
                                                   @PathVariable ServiceType serviceType) {
        log.info("Attempting to delete customer " + customerId + " service " + serviceType);

        int deletedCustomerServices = customerServiceRepository.deleteByCustomerServiceType(serviceType, customerId);

        if (deletedCustomerServices == 1) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
