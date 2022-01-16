package com.ninjaone.serverapp.services;

import com.ninjaone.serverapp.exceptions.ServiceCostNotFoundException;
import com.ninjaone.serverapp.models.ServiceCost;
import com.ninjaone.serverapp.repository.ServiceCostRepository;
import com.ninjaone.serverapp.services.interfaces.ServiceCostAccessService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ServiceCostRecordDetailsService implements ServiceCostAccessService {

  private final ServiceCostRepository serviceCostRepository;

  public ServiceCostRecordDetailsService(ServiceCostRepository serviceCostRepository) {
    this.serviceCostRepository = serviceCostRepository;
  }

  @Override
  public List<ServiceCost> getServiceCosts() {
    List<ServiceCost> serviceCosts = serviceCostRepository.findAll();

    return serviceCosts;
  }

  @Override
  public ServiceCost getServiceCost(Long id) {
    ServiceCost serviceCost = serviceCostRepository.findById(id)
        .orElseThrow(() -> new ServiceCostNotFoundException(id));

    return serviceCost;
  }
}
