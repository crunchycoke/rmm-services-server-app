package com.ninjaone.serverapp.services.interfaces;

import com.ninjaone.serverapp.models.ServiceCost;
import java.util.List;

public interface ServiceCostAccessService {
  
  public abstract List<ServiceCost> getServiceCosts();

  public abstract ServiceCost getServiceCost(Long id);
}
