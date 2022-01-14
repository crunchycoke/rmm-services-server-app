package com.ninjaone.serverapp.exceptions;

public class CustomerServiceNotFoundException extends RuntimeException {

    public CustomerServiceNotFoundException(Long id) {
        super("Customer service '" + id + "' not found");
    }
}