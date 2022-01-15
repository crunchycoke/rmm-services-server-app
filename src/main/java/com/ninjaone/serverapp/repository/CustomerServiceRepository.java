package com.ninjaone.serverapp.repository;

import com.ninjaone.serverapp.models.CustomerService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Long> {
}
