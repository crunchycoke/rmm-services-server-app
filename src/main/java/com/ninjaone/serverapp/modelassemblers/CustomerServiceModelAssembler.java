package com.ninjaone.serverapp.modelassemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ninjaone.serverapp.controllers.CustomerServiceController;
import com.ninjaone.serverapp.models.CustomerService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class CustomerServiceModelAssembler implements
    RepresentationModelAssembler<CustomerService, EntityModel<CustomerService>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityModel<CustomerService> toModel(CustomerService customerService) {
        return EntityModel.of(customerService,
            linkTo(methodOn(CustomerServiceController.class)
                .getCustomerServiceById(null, customerService.getCustomer().getId(),
                    customerService.getId())).withSelfRel(),
            linkTo(methodOn(CustomerServiceController.class)
                .getAllCustomerServices()).withRel("customerServices"));
    }
}
