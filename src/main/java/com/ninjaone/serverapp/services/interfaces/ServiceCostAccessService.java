package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.models.ServiceCost;
import java.util.List;

/**
 * Service used for retrieving required data from the Service Cost table. Allows retrieval of all
 * service costs and specific service costs by service cost ID.
 */
public interface ServiceCostAccessService {

  /**
   * Retrieves all service costs and their information stored within the Service Cost table.
   *
   * @return A list of all Service Cost objects stored within the database.
   */
  List<ServiceCost> getServiceCosts();

  /**
   * Retrieves the specific Service Cost object using the provided Service Cost ID value.
   *
   * @param id Represents the Service Cost ID value.
   * @return A Service Cost object retrieved using the ID provided.
   */
  ServiceCost getServiceCost(Long id);
}
