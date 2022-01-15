package com.ninjaone.serverapp.controllers;

import com.ninjaone.serverapp.exceptions.CustomerDeviceNotFoundException;
import com.ninjaone.serverapp.exceptions.CustomerNotFoundException;
import com.ninjaone.serverapp.modelassemblers.CustomerDeviceModelAssembler;
import com.ninjaone.serverapp.models.Customer;
import com.ninjaone.serverapp.models.CustomerDevice;
import com.ninjaone.serverapp.repository.CustomerDeviceRepository;
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
public class CustomerDeviceController {

    private static final Logger log = LoggerFactory.getLogger(CustomerDeviceController.class);

    private final CustomerRepository customerRepository;
    private final CustomerDeviceRepository customerDeviceRepository;
    private final CustomerDeviceModelAssembler customerDeviceAssembler;

    public CustomerDeviceController(CustomerRepository customerRepository, CustomerDeviceRepository customerDeviceRepository, CustomerDeviceModelAssembler customerDeviceAssembler) {
        this.customerRepository = customerRepository;
        this.customerDeviceRepository = customerDeviceRepository;
        this.customerDeviceAssembler = customerDeviceAssembler;
    }

    @GetMapping("/customers/devices")
    public CollectionModel<EntityModel<CustomerDevice>> getAllCustomerDevices() {
        log.info("Attempting to get all customer devices.");

        List<EntityModel<CustomerDevice>> customerDevices =
                customerDeviceRepository.findAll().stream()
                        .map(customerDeviceAssembler::toModel)
                        .collect(Collectors.toList());

        return CollectionModel.of(customerDevices, linkTo(methodOn(CustomerDeviceController.class)
                .getAllCustomerDevices()).withSelfRel());
    }

    @GetMapping("/customers/{customerId}/devices")
    public CollectionModel<EntityModel<CustomerDevice>> getCustomerDeviceByCustomerId(@PathVariable Long customerId) {
        log.info("Attempting to get all customer " + customerId + " devices.");

        List<EntityModel<CustomerDevice>> customerDevices =
                customerDeviceRepository.getCustomerDevicesByCustomerId(customerId).stream()
                        .map(customerDeviceAssembler::toModel)
                        .collect(Collectors.toList());

        if (customerDevices.isEmpty()) {
            throw new CustomerDeviceNotFoundException(customerId);
        }

        return CollectionModel.of(customerDevices, linkTo(methodOn(CustomerDeviceController.class)
                .getAllCustomerDevices()).withSelfRel());
    }

    @GetMapping("/customers/{customerId}/devices/{id}")
    public EntityModel<CustomerDevice> getCustomerDeviceById(@PathVariable Long customerId,
                                                             @PathVariable Long id) {
        log.info("Attempting to get customer " + customerId + " device " + id);

        CustomerDevice customerDevice = customerDeviceRepository.getCustomerDeviceById(id, customerId)
                .orElseThrow(() -> new CustomerDeviceNotFoundException(id));

        return customerDeviceAssembler.toModel(customerDevice);
    }

    @PostMapping("/customers/{customerId}/devices")
    public ResponseEntity<?> addCustomerDevice(@RequestBody CustomerDevice newCustomerDevice,
                                               @PathVariable Long customerId) {
        log.info("Attempting to add customer device " + newCustomerDevice);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        newCustomerDevice.setCustomer(customer);

        EntityModel<CustomerDevice> customerDeviceEntityModel =
                customerDeviceAssembler.toModel(customerDeviceRepository.save(newCustomerDevice));

        return ResponseEntity.created(customerDeviceEntityModel
                        .getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(customerDeviceEntityModel);
    }

    @PutMapping("/customers/{customerId}/devices/{id}")
    public ResponseEntity<?> replaceCustomerDevice(@RequestBody CustomerDevice newCustomerDevice,
                                                   @PathVariable Long customerId,
                                                   @PathVariable Long id) {
        log.info("Attempting to add customer device " + newCustomerDevice);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        newCustomerDevice.setCustomer(customer);

        CustomerDevice updatedCustomerDevice = customerDeviceRepository.getCustomerDeviceById(id, customerId)
                .map(customerDevice -> {
                    customerDevice.setDeviceType(newCustomerDevice.getDeviceType());
                    customerDevice.setSystemName(newCustomerDevice.getSystemName());

                    log.info("Attempting to add customer device.");

                    return customerDeviceRepository.save(customerDevice);
                })
                .orElseGet(() -> {
                    newCustomerDevice.setId(id);
                    return customerDeviceRepository.save(newCustomerDevice);
                });

        EntityModel<CustomerDevice> customerDeviceEntityModel = customerDeviceAssembler.toModel(updatedCustomerDevice);
        return ResponseEntity.created(customerDeviceEntityModel
                .getRequiredLink(IanaLinkRelations.SELF)
                .toUri()).body(customerDeviceEntityModel);
    }

    @DeleteMapping("/customers/{customerId}/devices/{id}")
    public ResponseEntity<?> deleteCustomerDevice(@PathVariable Long customerId,
                                                  @PathVariable Long id) {
        log.info("Attempting to delete customer " + customerId + " device " + id);

        int deletedCustomerDevices = customerDeviceRepository.deleteByDeviceId(id, customerId);

        if (deletedCustomerDevices == 1) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
