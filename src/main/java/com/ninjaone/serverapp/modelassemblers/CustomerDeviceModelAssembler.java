package com.ninjaone.serverapp.modelassemblers;

import com.ninjaone.serverapp.controllers.CustomerDeviceController;
import com.ninjaone.serverapp.models.CustomerDevice;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerDeviceModelAssembler implements RepresentationModelAssembler<CustomerDevice, EntityModel<CustomerDevice>> {

    @Override
    public EntityModel<CustomerDevice> toModel(CustomerDevice customerDevice) {
        return EntityModel.of(customerDevice,
                linkTo(methodOn(CustomerDeviceController.class)
                        .getCustomerDeviceById(customerDevice.getId())).withSelfRel(),
                linkTo(methodOn(CustomerDeviceController.class)
                        .getAllCustomerDevices()).withRel("customerDevices"));
    }
}
