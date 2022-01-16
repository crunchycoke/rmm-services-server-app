package com.ninjaone.serverapp.exceptions;

/**
 *
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Customer '" + id + "' not found");
    }
}
