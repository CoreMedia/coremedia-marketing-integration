package com.coremedia.blueprint.marketing.impl;

import com.coremedia.blueprint.marketing.MarketingContext;

import java.util.Map;

public class MarketingContextImpl implements MarketingContext {
  private static final String CONNECTION_ID = "connectionId";
  private static final String CONNECTION_TYPE = "connectionType";
  private static final String ENABLED = "enabled";
  private static final String VENDOR_NAME = "vendorName";
  private static final String VENDOR_URL = "vendorUrl";
  private static final String VENDOR_VERSION = "vendorVersion";

  private Map<String, Object> properties;

  MarketingContextImpl(Map<String, Object> properties) {
    this.properties = properties;
  }

  @Override
  public String getConnectionId() {
    return (String) properties.get(CONNECTION_ID);
  }

  @Override
  public String getConnectionType() {
    return (String) properties.get(CONNECTION_TYPE);
  }

  @Override
  public String getProperty(String key) {
    return (String) properties.get(key);
  }

  @Override
  public boolean isEnabled() {
    if(properties.containsKey(ENABLED)) {
      return (Boolean) properties.get(ENABLED);
    }
    return true;
  }

  @Override
  public String getVendorName() {
    return (String) properties.get(VENDOR_NAME);
  }

  @Override
  public String getVendorUrl() {
    return (String) properties.get(VENDOR_URL);
  }

  @Override
  public String getVendorVersion() {
    return (String) properties.get(VENDOR_VERSION);
  }
}
