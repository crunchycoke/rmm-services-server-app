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
 *
 */
@RestController
public class CustomerDeviceController {

    private static final Logger log = LoggerFactory.getLogger(CustomerDeviceController.class);

    private final CustomerDeviceRecordDetailsService customerDeviceRecordDetailsService;
    private final CustomerRecordDetailsService customerRecordDetailsService;
    private final CustomerDeviceModelAssembler customerDeviceAssembler;

    /**
     *
     * @param customerDeviceRecordDetailsService
     * @param customerRecordDetailsService
     * @param customerDeviceAssembler
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
     *
     * @return
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
     *
     * @param authentication
     * @param customerId
     * @return
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
     *
     * @param authentication
     * @param customerId
     * @param id
     * @return
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
     *
     * @param authentication
     * @param newCustomerDevice
     * @param customerId
     * @return
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
     *
     * @param authentication
     * @param newCustomerDevice
     * @param customerId
     * @param id
     * @return
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
     * @param authentication
     * @param customerId
     * @param id
     * @return
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
