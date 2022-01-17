package com.ninjaone.serverapp.exceptions;

/**
 * Thrown when the provided customer ID is not found or not accessible with the authorization
 * provided.
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Customer '" + id + "' not found or authorization invalid.");
    }
}
