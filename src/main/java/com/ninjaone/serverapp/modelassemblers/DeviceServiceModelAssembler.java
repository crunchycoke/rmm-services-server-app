package com.ninjaone.serverapp.modelassemblers;

import com.ninjaone.serverapp.controllers.DeviceServiceController;
import com.ninjaone.serverapp.models.DeviceService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DeviceServiceModelAssembler implements RepresentationModelAssembler<DeviceService, EntityModel<DeviceService>> {

    @Override
    public EntityModel<DeviceService> toModel(DeviceService deviceService) {
        return EntityModel.of(deviceService,
                linkTo(methodOn(DeviceServiceController.class)
                        .getDeviceServicesById(deviceService.getId())).withSelfRel(),
                linkTo(methodOn(DeviceServiceController.class)
                        .getAllDeviceServices()).withRel("deviceServices"));
    }
}
