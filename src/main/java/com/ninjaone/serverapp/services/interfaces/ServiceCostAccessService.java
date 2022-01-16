package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.models.ServiceCost;
import java.util.List;

/**
 *
 */
public interface ServiceCostAccessService {

  /**
   * @return
   */
  public abstract List<ServiceCost> getServiceCosts();

  /**
   * @param id
   * @return
   */
  public abstract ServiceCost getServiceCost(Long id);
}
