package com.ninjaone.serverapp.modelassemblers;

import com.ninjaone.serverapp.controllers.DeviceServiceController;
import com.ninjaone.serverapp.models.ServiceCost;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DeviceServiceModelAssembler implements RepresentationModelAssembler<ServiceCost, EntityModel<ServiceCost>> {

    @Override
    public EntityModel<ServiceCost> toModel(ServiceCost serviceCost) {
        return EntityModel.of(serviceCost,
                linkTo(methodOn(DeviceServiceController.class)
                        .getDeviceServiceById(serviceCost.getId())).withSelfRel(),
                linkTo(methodOn(DeviceServiceController.class)
                        .getAllDeviceServices()).withRel("deviceServices"));
    }
}
