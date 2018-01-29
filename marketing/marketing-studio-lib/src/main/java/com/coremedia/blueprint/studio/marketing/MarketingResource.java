package com.coremedia.blueprint.studio.marketing;

import com.coremedia.blueprint.marketing.MarketingConnection;
import com.coremedia.blueprint.marketing.MarketingContext;
import com.coremedia.blueprint.marketing.impl.MarketingContextProvider;
import com.coremedia.blueprint.studio.marketing.model.MarketingModel;
import com.coremedia.blueprint.studio.marketing.representation.MarketingRepresentation;
import com.coremedia.rest.linking.EntityResource;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.coremedia.blueprint.studio.marketing.MarketingResource.CONNECTION_ID;

/**
 * A resource to retrieve the basic information of a marketing system.
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("marketing/marketing/{" + CONNECTION_ID + ":[^/]+}")
public class MarketingResource extends AbstractMarketingResource implements EntityResource<MarketingModel> {
  public static final String CONNECTION_ID = "connectionId";

  private String connectionId;
  private MarketingContextProvider contextProvider;
  private MarketingModel entity;

  @PathParam(CONNECTION_ID)
  public void setConnectionId(final String connectionId) {
    this.connectionId = connectionId;
  }

  public String getConnectionId() {
    return entity.getConnectionId();
  }

  @GET
  public MarketingRepresentation get() throws Exception {
    MarketingRepresentation marketingRepresentation = new MarketingRepresentation();
    MarketingModel entity = getEntity();
    marketingRepresentation.setType(entity.getType());
    marketingRepresentation.setConnectionId(entity.getConnectionId());
    marketingRepresentation.setId(entity.getConnectionId());

    MarketingContext context = getConnection().getMarketingContext();
    marketingRepresentation.setVendorName(context.getVendorName());
    marketingRepresentation.setVendorUrl(context.getVendorUrl());
    marketingRepresentation.setVendorVersion(context.getVendorVersion());

    return marketingRepresentation;
  }

  private MarketingConnection getConnection() {
    MarketingContext context = contextProvider.findContext(connectionId);
    return getMarketing().getConnection(context);
  }

  @Required
  public void setContextProvider(MarketingContextProvider contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MarketingModel getEntity() {
    return new MarketingModel(getConnection());
  }

  @Override
  public void setEntity(MarketingModel entity) {
    this.entity = entity;
  }
}
