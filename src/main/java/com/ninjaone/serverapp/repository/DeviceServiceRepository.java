package com.ninjaone.serverapp.repository;

import com.ninjaone.serverapp.models.ServiceCost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceServiceRepository extends JpaRepository<ServiceCost, Long> {
}
