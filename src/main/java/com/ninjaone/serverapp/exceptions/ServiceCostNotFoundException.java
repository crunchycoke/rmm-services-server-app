package com.ninjaone.serverapp.exceptions;

import com.ninjaone.serverapp.enums.ServiceType;

public class ServiceCostNotFoundException extends RuntimeException {

    public ServiceCostNotFoundException(Long id) {
        super("Service cost '" + id + "' not found");
    }

    public ServiceCostNotFoundException(ServiceType serviceType) {
        super("Service cost '" + serviceType.toString() + "' not found");
    }
}
