package com.coremedia.blueprint.marketing;

/**
 * Contains the configuration values for a marketing connection.
 */
public interface MarketingContext {
  String getConnectionId();

  String getConnectionType();

  String getProperty(String key);

  boolean isEnabled();

  String getVendorName();

  String getVendorUrl();

  String getVendorVersion();
}
