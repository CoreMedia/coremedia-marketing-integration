package com.coremedia.blueprint.studio.marketing.representation;

import com.coremedia.blueprint.studio.marketing.model.MarketingModel;

import java.util.List;

public class MarketingsRepresentation extends AbstractMarketingResourceRepresentation {

  List<MarketingModel> marketings;

  public List<MarketingModel> getMarketings() {
    return marketings;
  }

  public void setMarketings(List<MarketingModel> marketings) {
    this.marketings = marketings;
  }
}
