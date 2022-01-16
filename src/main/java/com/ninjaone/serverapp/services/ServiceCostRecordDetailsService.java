package com.ninjaone.serverapp.services;

import com.ninjaone.serverapp.exceptions.ServiceCostNotFoundException;
import com.ninjaone.serverapp.models.ServiceCost;
import com.ninjaone.serverapp.repository.ServiceCostRepository;
import com.ninjaone.serverapp.services.interfaces.ServiceCostAccessService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class ServiceCostRecordDetailsService implements ServiceCostAccessService {

  private static final Logger log =
      LoggerFactory.getLogger(ServiceCostRecordDetailsService.class);

  private final ServiceCostRepository serviceCostRepository;

  /**
   * Constructs the service used for accessing the records and details within the service cost
   * table.
   *
   * @param serviceCostRepository Represents the repository used for accessing the service cost
   *                              table loaded through dependency injection.
   */
  public ServiceCostRecordDetailsService(ServiceCostRepository serviceCostRepository) {
    this.serviceCostRepository = serviceCostRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ServiceCost> getServiceCosts() {
    List<ServiceCost> serviceCosts = serviceCostRepository.findAll();

    log.info("Retrieved all available service costs.");

    return serviceCosts;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ServiceCost getServiceCost(Long id) {
    ServiceCost serviceCost = serviceCostRepository.findById(id)
        .orElseThrow(() -> new ServiceCostNotFoundException(id));

    log.info("Retrieved service cost using service cost " + id + ".");

    return serviceCost;
  }
}
