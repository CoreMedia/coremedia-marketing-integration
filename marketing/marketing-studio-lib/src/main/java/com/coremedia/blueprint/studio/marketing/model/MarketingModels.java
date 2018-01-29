package com.coremedia.blueprint.studio.marketing.model;

import com.coremedia.blueprint.marketing.MarketingConnection;
import com.coremedia.blueprint.marketing.MarketingConnections;

import java.util.ArrayList;
import java.util.List;

/**
 * Multiple Marketing for REST
 */
public class MarketingModels {

  private List<MarketingModel> marketings;

  public MarketingModels(MarketingConnections connectionsForSite) {
    for (MarketingConnection c : connectionsForSite.getMarketingConnections()) {
      getMarketings().add(new MarketingModel(c));
    }
  }

  public List<MarketingModel> getMarketings() {
    if (marketings == null) {
      marketings = new ArrayList<>();
    }
    return marketings;
  }
}
