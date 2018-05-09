package com.coremedia.blueprint.studio.marketing;

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
