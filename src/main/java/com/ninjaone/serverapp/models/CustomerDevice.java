package com.ninjaone.serverapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ninjaone.serverapp.enums.DeviceType;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents a customer device with a user defined ID, system name, and device type values.
 * Includes a link to the customer.
 */
@Entity
@Table(name = "CUSTOMER_DEVICE")
public class CustomerDevice {

    @Id
    private Long id;

    private String systemName;
    private DeviceType deviceType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    public CustomerDevice() {
    }

    public CustomerDevice(Long id, String systemName, DeviceType deviceType, Customer customer) {
        this.id = id;
        this.systemName = systemName;
        this.deviceType = deviceType;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
            && Objects.equals(systemName, customerDevice.systemName)
            && deviceType == customerDevice.deviceType
            && Objects.equals(customer, customerDevice.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
            systemName,
            deviceType,
            customer);
    }

    @Override
    public String toString() {
        return "CustomerDevice{" +
            "id=" + id +
            ", systemName='" + systemName + '\'' +
            ", deviceType=" + deviceType +
            ", customer=" + customer +
            '}';
    }
}
