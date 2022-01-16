package com.ninjaone.serverapp.modelassemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.ninjaone.serverapp.controllers.CustomerController;
import com.ninjaone.serverapp.models.Customer;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class CustomerModelAssembler implements
    RepresentationModelAssembler<Customer, EntityModel<Customer>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityModel<Customer> toModel(Customer customer) {
        return EntityModel.of(customer,
            linkTo(methodOn(CustomerController.class).getCustomerById(null,
                customer.getId())).withSelfRel(),
            linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"));
    }
}
