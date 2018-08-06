package com.coremedia.blueprint.studio.marketing;


import com.coremedia.blueprint.studio.marketing.AbstractMarketingResourceRepresentation;

public class MarketingRepresentation extends AbstractMarketingResourceRepresentation {
  private String connectionId;
  private String vendorName;
  private String vendorVersion;
  private String vendorUrl;

  public String getConnectionId() {
    return connectionId;
  }

  public void setConnectionId(String connectionId) {
    this.connectionId = connectionId;
  }

  public String getVendorName() {
    return vendorName;
  }

  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
  }

  public String getVendorVersion() {
    return vendorVersion;
  }

  public void setVendorVersion(String vendorVersion) {
    this.vendorVersion = vendorVersion;
  }

  public String getVendorUrl() {
    return vendorUrl;
  }

  public void setVendorUrl(String vendorUrl) {
    this.vendorUrl = vendorUrl;
  }
}
