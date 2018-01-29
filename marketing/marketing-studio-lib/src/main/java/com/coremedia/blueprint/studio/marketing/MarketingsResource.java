package com.coremedia.blueprint.studio.marketing;

import com.coremedia.blueprint.marketing.MarketingConnections;
import com.coremedia.blueprint.marketing.impl.MarketingConnectionsInitializer;
import com.coremedia.blueprint.studio.marketing.model.MarketingModels;
import com.coremedia.blueprint.studio.marketing.representation.MarketingsRepresentation;
import com.coremedia.cap.multisite.Site;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.rest.linking.EntityResource;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.coremedia.blueprint.studio.marketing.MarketingsResource.SITE_ID;

/**
 * A resource to receive the marketing systems available for a site.
 */
@Path("marketing/marketings/{" + SITE_ID + ":[^/]+}")
@Produces(MediaType.APPLICATION_JSON)
public class MarketingsResource extends AbstractMarketingResource implements EntityResource<MarketingModels> {
  public static final String SITE_ID = "siteId";
  private String siteId;

  private MarketingConnectionsInitializer connectionsInitializer;
  private SitesService sitesService;

  @GET
  public MarketingsRepresentation get() {
    MarketingsRepresentation marketingsRepresentation = new MarketingsRepresentation();
    MarketingModels entity = getEntity();
    marketingsRepresentation.setMarketings(entity.getMarketings());
    return marketingsRepresentation;
  }

  @Override
  public MarketingModels getEntity() {
    Site site = sitesService.getSite(getSiteId());
    MarketingConnections connectionsForSite = connectionsInitializer.findConnectionsForSite(site);
    return new MarketingModels(connectionsForSite);
  }

  @Override
  public void setEntity(MarketingModels marketings) {
    //not used here
  }

  @PathParam(SITE_ID)
  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public String getSiteId() {
    return siteId;
  }

  @Required
  public void setMarketingConnectionsInitializer(MarketingConnectionsInitializer connectionsInitializer) {
    this.connectionsInitializer = connectionsInitializer;
  }

  @Required
  public void setSitesService(SitesService sitesService) {
    this.sitesService = sitesService;
  }
}
