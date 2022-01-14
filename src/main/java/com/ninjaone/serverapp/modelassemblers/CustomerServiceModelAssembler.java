package com.ninjaone.serverapp.modelassemblers;

import com.ninjaone.serverapp.controllers.CustomerServiceController;
import com.ninjaone.serverapp.models.CustomerService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerServiceModelAssembler implements RepresentationModelAssembler<CustomerService, EntityModel<CustomerService>> {

    @Override
    public EntityModel<CustomerService> toModel(CustomerService customerService) {
        return EntityModel.of(customerService,
                linkTo(methodOn(CustomerServiceController.class)
                        .getCustomerServicesById(customerService.getCustomerId(), customerService.getId())).withSelfRel(),
                linkTo(methodOn(CustomerServiceController.class)
                        .getAllCustomerServices(customerService.getCustomerId())).withRel("customerServices"));
    }
}
