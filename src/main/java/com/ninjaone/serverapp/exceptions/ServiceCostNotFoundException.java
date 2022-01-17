package com.ninjaone.serverapp.exceptions;

import com.ninjaone.serverapp.enums.ServiceType;

/**
 * Thrown when the provided service cost ID is not found.
 */
public class ServiceCostNotFoundException extends RuntimeException {

    public ServiceCostNotFoundException(Long id) {
        super("Service cost '" + id + "' not found or authorization invalid.");
    }

    public ServiceCostNotFoundException(ServiceType serviceType) {
        super("Service cost '" + serviceType.toString() + "' not found or authorization invalid.");
    }
}
