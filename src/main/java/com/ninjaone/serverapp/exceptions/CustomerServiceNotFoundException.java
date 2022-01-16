package com.ninjaone.serverapp.exceptions;

/**
 * Thrown when the provided service ID is not found or is not attached to the request customer.
 */
public class CustomerServiceNotFoundException extends RuntimeException {

    public CustomerServiceNotFoundException(Long id) {
        super("Customer service '" + id + "' not found or authorization invalid.");
    }
}