package com.ninjaone.serverapp.models;

import com.ninjaone.serverapp.enums.DeviceType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "CUSTOMER_DEVICE")
public class CustomerDevice {

    @Id
    @GeneratedValue
    private Long id;

    private Long customerId;
    private String systemName;
    private DeviceType deviceType;

    public CustomerDevice() {
    }

    public CustomerDevice(Long customerId, String systemName, DeviceType deviceType) {
        this.customerId = customerId;
        this.systemName = systemName;
        this.deviceType = deviceType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CustomerDevice customerDevice)) {
            return false;
        }

        return Objects.equals(id, customerDevice.id)
                && Objects.equals(customerId, customerDevice.customerId)
                && Objects.equals(systemName, customerDevice.systemName)
                && deviceType == customerDevice.deviceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, systemName, deviceType);
    }

    @Override
    public String toString() {
        return "CustomerDevice{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", systemName='" + systemName + '\'' +
                ", deviceType=" + deviceType +
                '}';
    }
}
