package com.ninjaone.serverapp.repository;

import com.ninjaone.serverapp.enums.DeviceOperatingSystem;
import com.ninjaone.serverapp.enums.ServiceType;
import com.ninjaone.serverapp.models.ServiceCost;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository used for accessing data within the Service Cost table.
 */
public interface ServiceCostRepository extends JpaRepository<ServiceCost, Long> {

  // Get specific service cost
  @Query("select serviceCost.servicePrice " +
      "from ServiceCost serviceCost " +
      "where serviceCost.serviceType = :serviceType " +
      "and serviceCost.deviceOperatingSystem = :deviceOperatingSystem")
  Optional<BigDecimal> getServicePriceByTypeAndOs(@Param("serviceType") ServiceType serviceType,
      @Param("deviceOperatingSystem") DeviceOperatingSystem deviceOperatingSystem);
}
