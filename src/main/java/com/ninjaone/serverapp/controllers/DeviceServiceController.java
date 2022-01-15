package com.ninjaone.serverapp.controllers;

import com.ninjaone.serverapp.exceptions.DeviceServiceNotFoundException;
import com.ninjaone.serverapp.modelassemblers.DeviceServiceModelAssembler;
import com.ninjaone.serverapp.models.ServiceCost;
import com.ninjaone.serverapp.repository.DeviceServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class DeviceServiceController {

    private static final Logger log = LoggerFactory.getLogger(DeviceServiceController.class);

    private final DeviceServiceRepository deviceServiceRepository;
    private final DeviceServiceModelAssembler deviceServiceModelAssembler;

    public DeviceServiceController(DeviceServiceRepository deviceServiceRepository,
                                   DeviceServiceModelAssembler deviceServiceModelAssembler) {
        this.deviceServiceRepository = deviceServiceRepository;
        this.deviceServiceModelAssembler = deviceServiceModelAssembler;
    }

    @GetMapping("/services")
    public CollectionModel<EntityModel<ServiceCost>> getAllDeviceServices() {
        log.info("Attempting to get all device services.");

        List<EntityModel<ServiceCost>> deviceServices =
                deviceServiceRepository.findAll().stream()
                        .map(deviceServiceModelAssembler::toModel)
                        .collect(Collectors.toList());

        return CollectionModel.of(deviceServices, linkTo(methodOn(DeviceServiceController.class)
                .getAllDeviceServices()).withSelfRel());
    }

    @GetMapping("/services/{id}")
    public EntityModel<ServiceCost> getDeviceServicesById(@PathVariable Long id) {
        log.info("Attempting to get device service " + id);

        ServiceCost serviceCost = deviceServiceRepository.findById(id)
                .orElseThrow(() -> new DeviceServiceNotFoundException(id));

        return deviceServiceModelAssembler.toModel(serviceCost);
    }
}
