package com.coremedia.blueprint.marketing.impl;

import com.coremedia.blueprint.marketing.MarketingConnections;
import com.coremedia.blueprint.marketing.MarketingContext;
import com.coremedia.cap.multisite.Site;
import org.springframework.beans.factory.annotation.Required;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.List;

public class MarketingConnectionsInitializer {
  private Marketing marketing;

  private MarketingContextProvider contextProvider;

  @NonNull
  public MarketingConnections findConnectionsForSite(@Nullable Site site) {
    List<MarketingContext> connectionIdsBySite = this.contextProvider.findContexts(site);
    return this.marketing.getConnections(connectionIdsBySite);
  }

  @Required
  public void setMarketingContextProvider(MarketingContextProvider contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Required
  public void setMarketing(Marketing marketing) {
    this.marketing = marketing;
  }
}
