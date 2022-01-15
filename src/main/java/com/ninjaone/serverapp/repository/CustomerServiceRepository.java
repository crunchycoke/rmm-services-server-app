package com.ninjaone.serverapp.repository;

import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.models.CustomerService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Long> {

    // Get all service from customer
    @Query("select service " +
            "from CustomerService service " +
            "where service.customer.id = :customerId")
    List<CustomerService> getCustomerServicesByCustomerId(@Param("customerId") Long customerId);

    // Get specific service from customer
    @Query("select service " +
            "from CustomerService service " +
            "where service.id = :id and service.customer.id = :customerId")
    Optional<CustomerService> getCustomerServiceById(@Param("id") Long id,
                                                     @Param("customerId") Long customerId);

    // Delete service tied to specific customer
    @Transactional
    @Modifying
    @Query("delete from CustomerService service " +
            "where service.serviceType = :serviceType " +
            "and service.customer.id = :customerId")
    int deleteByCustomerServiceId(@Param("serviceType") ServiceType serviceType,
                                  @Param("customerId") Long customerId);
}
