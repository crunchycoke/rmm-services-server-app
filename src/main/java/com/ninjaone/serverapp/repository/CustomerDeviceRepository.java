package com.ninjaone.serverapp.repository;

import com.ninjaone.serverapp.models.CustomerDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CustomerDeviceRepository extends JpaRepository<CustomerDevice, Long> {

    // Get all devices from customer
    @Query("select device " +
            "from CustomerDevice device " +
            "where device.customer.id = :customerId")
    List<CustomerDevice> getCustomerDevicesByCustomerId(@Param("customerId") Long customerId);

    // Get specific device from customer
    @Query("select device " +
            "from CustomerDevice device " +
            "where device.id = :id and device.customer.id = :customerId")
    Optional<CustomerDevice> getCustomerDeviceById(@Param("id") Long id,
                                                   @Param("customerId") Long customerId);

    // Delete device tied to specific customer
    @Transactional
    @Modifying
    @Query("delete from CustomerDevice device " +
            "where device.id = :id " +
            "and device.customer.id = :customerId")
    int deleteByDeviceId(@Param("id") Long id,
                         @Param("customerId") Long customerId);
}
