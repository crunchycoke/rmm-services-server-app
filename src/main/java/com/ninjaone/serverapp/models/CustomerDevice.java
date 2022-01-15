package com.ninjaone.serverapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ninjaone.serverapp.enums.DeviceType;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "CUSTOMER_DEVICE")
public class CustomerDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String systemName;
    private DeviceType deviceType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    public CustomerDevice() {
    }

    public CustomerDevice(String systemName, DeviceType deviceType, Customer customer) {
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
