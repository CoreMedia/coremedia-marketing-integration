package com.coremedia.blueprint.studio.marketing.model;

import com.coremedia.blueprint.marketing.MarketingConnection;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * A single Marketing representation for REST
 */
public class MarketingModel {

  private String type;
  private String connectionId;


  public MarketingModel(MarketingConnection connection) {
    type = connection.getType();
    connectionId = connection.getMarketingContext().getConnectionId();
  }

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  public String getType() {
    return type;
  }

  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  public String getConnectionId() {
    return connectionId;
  }
}
