package com.ninjaone.serverapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ninjaone.serverapp.enums.ServiceType;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "CUSTOMER_SERVICE")
public class CustomerService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceName;
    private ServiceType serviceType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    public CustomerService() {
    }

    public CustomerService(String serviceName, ServiceType serviceType, Customer customer) {
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.customer = customer;
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

        if (!(o instanceof CustomerService customerService)) {
            return false;
        }

        return Objects.equals(id, customerService.id)
            && Objects.equals(serviceName, customerService.serviceName)
            && Objects.equals(serviceType, customerService.serviceType)
            && Objects.equals(customer, customerService.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
            serviceName,
            serviceType,
            customer);
    }

    @Override
    public String toString() {
        return "CustomerService{" +
            "id=" + id +
            ", serviceName=" + serviceName +
            ", serviceType=" + serviceType +
            ", customer=" + customer +
            '}';
    }
}
