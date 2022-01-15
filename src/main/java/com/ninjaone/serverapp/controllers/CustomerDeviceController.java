package com.ninjaone.serverapp.controllers;

import com.ninjaone.serverapp.exceptions.CustomerDeviceNotFoundException;
import com.ninjaone.serverapp.modelassemblers.CustomerDeviceModelAssembler;
import com.ninjaone.serverapp.models.CustomerDevice;
import com.ninjaone.serverapp.repository.CustomerDeviceRepository;
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

    private final CustomerDeviceRepository customerDeviceRepository;
    private final CustomerDeviceModelAssembler customerDeviceAssembler;

    public CustomerDeviceController(CustomerDeviceRepository customerDeviceRepository, CustomerDeviceModelAssembler customerDeviceAssembler) {
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

    @GetMapping("/customers/devices/{id}")
    public EntityModel<CustomerDevice> getCustomerDeviceById(@PathVariable Long id) {
        log.info("Attempting to get customer device " + id);

        CustomerDevice customerDevice = customerDeviceRepository.findById(id)
                .orElseThrow(() -> new CustomerDeviceNotFoundException(id));

        return customerDeviceAssembler.toModel(customerDevice);
    }

    @PostMapping("/customers/{customerId}/devices")
    public ResponseEntity<?> addCustomerDevice(@RequestBody CustomerDevice newCustomerDevice,
                                               @PathVariable Long customerId) {
        log.info("Attempting to add customer device " + newCustomerDevice);

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
        CustomerDevice updatedCustomerDevice = customerDeviceRepository.findById(id)
                .map(customerDevice -> {
                    customerDevice.setDeviceType(newCustomerDevice.getDeviceType());
                    customerDevice.setSystemName(newCustomerDevice.getSystemName());
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
    public ResponseEntity<?> deleteEmployee(@PathVariable Long customerId,
                                            @PathVariable Long id) {
        customerDeviceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
