package com.ninjaone.serverapp.models;

import com.ninjaone.serverapp.enums.DeviceOperatingSystem;
import com.ninjaone.serverapp.enums.ServiceType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "SERVICE_COST")
public class ServiceCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private ServiceType serviceType;
    private DeviceOperatingSystem deviceOperatingSystem;
    private BigDecimal servicePrice;

    public ServiceCost() {
    }

    public ServiceCost(String serviceName,
                       ServiceType serviceType,
                       DeviceOperatingSystem deviceOperatingSystem,
                       BigDecimal servicePrice) {
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.deviceOperatingSystem = deviceOperatingSystem;
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

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public DeviceOperatingSystem getDeviceOperatingSystem() {
        return deviceOperatingSystem;
    }

    public void setDeviceOperatingSystem(DeviceOperatingSystem deviceOperatingSystem) {
        this.deviceOperatingSystem = deviceOperatingSystem;
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
                && Objects.equals(serviceType, serviceCost.serviceType)
                && Objects.equals(deviceOperatingSystem, serviceCost.deviceOperatingSystem)
                && Objects.equals(servicePrice, serviceCost.servicePrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                serviceName,
                serviceType,
                deviceOperatingSystem,
                servicePrice);
    }

    @Override
    public String toString() {
        return "ServiceCost{" +
                "id=" + id +
                ", serviceName='" + serviceName + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", serviceDeviceType='" + deviceOperatingSystem + '\'' +
                ", serviceCost=" + servicePrice +
                '}';
    }
}
