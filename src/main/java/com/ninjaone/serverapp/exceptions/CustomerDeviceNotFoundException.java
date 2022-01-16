package com.ninjaone.serverapp.exceptions;

/**
 *
 */
public class CustomerDeviceNotFoundException extends RuntimeException {

    public CustomerDeviceNotFoundException(Long id) {
        super("Customer device '" + id + "' not found");
    }
}
