package com.ninjaone.serverapp.models;

import com.ninjaone.serverapp.enums.ServiceDeviceType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "SERVICE_COST")
public class ServiceCost {

    @Id
    @GeneratedValue
    private Long id;

    private String serviceName;
    private ServiceDeviceType serviceDeviceType;
    private BigDecimal servicePrice;

    public ServiceCost() {
    }

    public ServiceCost(String serviceName, ServiceDeviceType serviceDeviceType,
                       BigDecimal servicePrice) {
        this.serviceName = serviceName;
        this.serviceDeviceType = serviceDeviceType;
        this.servicePrice = servicePrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceDeviceType getServiceDeviceType() {
        return serviceDeviceType;
    }

    public void setServiceDeviceType(ServiceDeviceType serviceDeviceType) {
        this.serviceDeviceType = serviceDeviceType;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal serviceCost) {
        this.servicePrice = serviceCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ServiceCost serviceCost)) {
            return false;
        }

        return Objects.equals(id, serviceCost.id)
                && Objects.equals(serviceName, serviceCost.serviceName)
                && Objects.equals(serviceDeviceType, serviceCost.serviceDeviceType)
                && Objects.equals(servicePrice, serviceCost.servicePrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceName, serviceDeviceType, servicePrice);
    }

    @Override
    public String toString() {
        return "ServiceCost{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", serviceDeviceType='" + serviceDeviceType + '\'' +
                ", serviceCost=" + servicePrice +
                '}';
    }
}
