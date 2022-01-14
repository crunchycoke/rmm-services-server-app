package com.ninjaone.serverapp.exceptions;

public class DeviceServiceNotFoundException extends RuntimeException {
    public DeviceServiceNotFoundException(Long id) {
        super("Device service '" + id + "' not found");
    }
}
