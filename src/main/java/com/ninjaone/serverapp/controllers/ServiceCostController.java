package com.ninjaone.serverapp.controllers;

import com.ninjaone.serverapp.exceptions.ServiceCostNotFoundException;
import com.ninjaone.serverapp.modelassemblers.ServiceCostModelAssembler;
import com.ninjaone.serverapp.models.ServiceCost;
import com.ninjaone.serverapp.repository.ServiceCostRepository;
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
public class ServiceCostController {

    private static final Logger log = LoggerFactory.getLogger(ServiceCostController.class);

    private final ServiceCostRepository serviceCostRepository;
    private final ServiceCostModelAssembler serviceCostModelAssembler;

    public ServiceCostController(ServiceCostRepository serviceCostRepository,
                                 ServiceCostModelAssembler serviceCostModelAssembler) {
        this.serviceCostRepository = serviceCostRepository;
        this.serviceCostModelAssembler = serviceCostModelAssembler;
    }

    @GetMapping("/services")
    public CollectionModel<EntityModel<ServiceCost>> getAllServiceCosts() {
        log.info("Attempting to get all device services.");

        List<EntityModel<ServiceCost>> deviceServices =
                serviceCostRepository.findAll().stream()
                        .map(serviceCostModelAssembler::toModel)
                        .collect(Collectors.toList());

        return CollectionModel.of(deviceServices, linkTo(methodOn(ServiceCostController.class)
                .getAllServiceCosts()).withSelfRel());
    }

    @GetMapping("/services/{id}")
    public EntityModel<ServiceCost> getServiceCostById(@PathVariable Long id) {
        log.info("Attempting to get device service " + id);

        ServiceCost serviceCost = serviceCostRepository.findById(id)
                .orElseThrow(() -> new ServiceCostNotFoundException(id));

        return serviceCostModelAssembler.toModel(serviceCost);
    }
}