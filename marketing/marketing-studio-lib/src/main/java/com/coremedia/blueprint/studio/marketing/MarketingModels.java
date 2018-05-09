package com.coremedia.blueprint.studio.marketing;

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
      MarketingModel marketingModel = new MarketingModel(c);
      getMarketings().add(marketingModel);
    }
  }

  public List<MarketingModel> getMarketings() {
    if (marketings == null) {
      marketings = new ArrayList<>();
    }
    return marketings;
  }
}
