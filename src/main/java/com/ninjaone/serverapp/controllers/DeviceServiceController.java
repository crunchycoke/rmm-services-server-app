package com.ninjaone.serverapp.controllers;

import com.ninjaone.serverapp.exceptions.DeviceServiceNotFoundException;
import com.ninjaone.serverapp.modelassemblers.DeviceServiceModelAssembler;
import com.ninjaone.serverapp.models.DeviceService;
import com.ninjaone.serverapp.repository.DeviceServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
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
    public CollectionModel<EntityModel<DeviceService>> getAllDeviceServices() {
        log.debug("Attempting to get all device services.");

        List<EntityModel<DeviceService>> deviceServices =
                deviceServiceRepository.findAll().stream()
                        .map(deviceServiceModelAssembler::toModel)
                        .collect(Collectors.toList());

        return CollectionModel.of(deviceServices, linkTo(methodOn(DeviceServiceController.class)
                .getAllDeviceServices()).withSelfRel());
    }

    public EntityModel<DeviceService> getDeviceServicesById(Long id) {
        log.debug("Attempting to get device service " + id);

        DeviceService deviceService = deviceServiceRepository.findById(id)
                .orElseThrow(() -> new DeviceServiceNotFoundException(id));

        return deviceServiceModelAssembler.toModel(deviceService);
    }
}
