package com.ninjaone.serverapp.repository;

import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.models.CustomerService;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 */
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

    // Get specific service from customer
    @Query("select service " +
            "from CustomerService service " +
            "where service.serviceType = :serviceType and service.customer.id = :customerId")
    Optional<CustomerService> getCustomerServiceByName(@Param("serviceType") ServiceType serviceType,
                                                       @Param("customerId") Long customerId);

    // Insert service tied to specific customer
    @Transactional
    @Modifying
    @Query(value = "insert into CUSTOMER_SERVICE (service_name, service_type, customer_id) " +
            "values (:service_name, :service_type, :customer_id)", nativeQuery = true)
    int insertNewCustomerService(@Param("service_name") String serviceName,
                                 @Param("service_type") Integer serviceType,
                                 @Param("customer_id") Long customerId);

    // Delete service tied to specific customer
    @Transactional
    @Modifying
    @Query("delete from CustomerService service " +
            "where service.serviceType = :serviceType " +
            "and service.customer.id = :customerId")
    int deleteByCustomerServiceType(@Param("serviceType") ServiceType serviceType,
                                    @Param("customerId") Long customerId);
}
