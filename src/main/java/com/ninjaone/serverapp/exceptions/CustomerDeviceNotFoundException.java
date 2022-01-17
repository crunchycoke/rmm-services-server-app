package com.ninjaone.serverapp.exceptions;

/**
 * Thrown when the provided device ID is not found or is not attached to the request customer.
 */
public class CustomerDeviceNotFoundException extends RuntimeException {

    public CustomerDeviceNotFoundException(Long id) {
        super("Customer device '" + id + "' not found or authorization invalid.");
    }
}
