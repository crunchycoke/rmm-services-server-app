package com.ninjaone.serverapp.repository;

import com.ninjaone.serverapp.models.CustomerDevice;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository used for accessing data within the Customer Device table.
 */
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

    // Insert device tied to specific customer
    @Transactional
    @Modifying
    @Query(value = "insert into CUSTOMER_DEVICE (id, system_name, device_type, customer_id) " +
            "values (:id, :system_name, :device_type, :customer_id)", nativeQuery = true)
    int insertNewCustomerDevice(@Param("id") Long id,
                                @Param("system_name") String systemName,
                                @Param("device_type") Integer deviceType,
                                @Param("customer_id") Long customerId);

    // Delete device tied to specific customer
    @Transactional
    @Modifying
    @Query("delete from CustomerDevice device " +
            "where device.id = :id " +
            "and device.customer.id = :customerId")
    int deleteByCustomerDeviceId(@Param("id") Long id,
                                 @Param("customerId") Long customerId);
}
