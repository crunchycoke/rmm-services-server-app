package com.ninjaone.serverapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.util.StringUtils;

/**
 * Represents the customer object with a first name, middle name, last name, username, and password.
 * Includes links to both a list of customer devices and customer services.
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;
  private String middleName;
  private String lastName;

  @Column(unique = true)
  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<CustomerDevice> customerDevices = new ArrayList<>();

  @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<CustomerService> customerServices = new ArrayList<>();

  public Customer() {
  }

  public Customer(String firstName, String middleName, String lastName,
      String username, String password) {
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<CustomerDevice> getCustomerDevices() {
    return customerDevices;
  }

  public void setCustomerDevices(List<CustomerDevice> customerDevices) {
    this.customerDevices = customerDevices;
  }

  public List<CustomerService> getCustomerServices() {
    return customerServices;
  }

  public void setCustomerServices(List<CustomerService> customerServices) {
    this.customerServices = customerServices;
  }

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  public boolean isInvalidCustomerProfile() {
    if (!StringUtils.hasText(username)
        || !StringUtils.hasText(password)
        || !StringUtils.hasText(firstName)) {
      return true;
    }

    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Customer customer)) {
      return false;
    }

    return Objects.equals(id, customer.id)
        && Objects.equals(firstName, customer.firstName)
        && Objects.equals(middleName, customer.middleName)
        && Objects.equals(lastName, customer.lastName)
        && Objects.equals(username, customer.username)
        && Objects.equals(password, customer.password)
        && Objects.equals(customerDevices, customer.customerDevices)
        && Objects.equals(customerServices, customer.customerServices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id,
        firstName,
        middleName,
        lastName,
        username,
        password,
        customerDevices,
        customerServices);
  }

  @Override
  public String toString() {
    return "Customer{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", middleName='" + middleName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", username='" + username + '\'' +
        '}';
  }
}
