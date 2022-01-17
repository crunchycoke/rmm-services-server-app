package com.ninjaone.serverapp.modelassemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ninjaone.serverapp.controllers.ServiceCostController;
import com.ninjaone.serverapp.models.ServiceCost;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Assembles a Service Cost object into an EntityModel form.
 */
@Component
public class ServiceCostModelAssembler implements
    RepresentationModelAssembler<ServiceCost, EntityModel<ServiceCost>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityModel<ServiceCost> toModel(ServiceCost serviceCost) {
        return EntityModel.of(serviceCost,
            linkTo(methodOn(ServiceCostController.class)
                .getServiceCostById(serviceCost.getId())).withSelfRel(),
            linkTo(methodOn(ServiceCostController.class)
                .getAllServiceCosts()).withRel("serviceCosts"));
    }
}
