package com.ninjaone.serverapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "CUSTOMER_SERVICE")
public class CustomerService {

    @Id
    @GeneratedValue
    private Long id;

    private Long customerId;
    private Long deviceId;
    private Long serviceId;

    public CustomerService() {
    }

    public CustomerService(Long customerId, Long deviceId, Long serviceId) {
        this.customerId = customerId;
        this.deviceId = deviceId;
        this.serviceId = serviceId;
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

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CustomerService customerService)) {
            return false;
        }

        return Objects.equals(id, customerService.id)
                && Objects.equals(customerId, customerService.customerId)
                && Objects.equals(deviceId, customerService.deviceId)
                && Objects.equals(serviceId, customerService.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                customerId,
                deviceId,
                serviceId);
    }

    @Override
    public String toString() {
        return "CustomerService{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", deviceId=" + deviceId +
                ", serviceId=" + serviceId +
                '}';
    }
}
