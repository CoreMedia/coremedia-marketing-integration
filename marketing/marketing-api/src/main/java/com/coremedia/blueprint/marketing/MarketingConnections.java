package com.coremedia.blueprint.marketing;

import java.util.Collection;
import java.util.Map;

/**
 * A wrapper representing all the connections available in the current context (usually a Site).
 */
public class MarketingConnections {

  private Map<String, MarketingConnection> connectionMap;

  public MarketingConnections(Map<String, MarketingConnection> connections){
    this.connectionMap = connections;
  }

  /**
   * Returns the list of available marketing connections.
   * @return the list of connection configured in the MarketingConnection settings document.
   */
  public Collection<MarketingConnection> getMarketingConnections() {
    return connectionMap.values();
  }
}
