package com.ninjaone.serverapp.repository;

import com.ninjaone.serverapp.models.CustomerDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDeviceRepository extends JpaRepository<CustomerDevice, Long> {
}
