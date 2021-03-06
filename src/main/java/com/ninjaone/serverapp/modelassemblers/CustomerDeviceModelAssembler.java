package com.ninjaone.serverapp.modelassemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ninjaone.serverapp.controllers.CustomerDeviceController;
import com.ninjaone.serverapp.models.CustomerDevice;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Assembles a Customer Device object into an EntityModel form.
 */
@Component
public class CustomerDeviceModelAssembler implements
    RepresentationModelAssembler<CustomerDevice, EntityModel<CustomerDevice>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityModel<CustomerDevice> toModel(CustomerDevice customerDevice) {
        return EntityModel.of(customerDevice,
            linkTo(methodOn(CustomerDeviceController.class)
                .getCustomerDeviceById(null, customerDevice.getCustomer().getId(),
                    customerDevice.getId())).withSelfRel(),
            linkTo(methodOn(CustomerDeviceController.class)
                .getAllCustomerDevices()).withRel("customerDevices"));
    }
}
