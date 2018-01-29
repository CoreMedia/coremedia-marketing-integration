package com.coremedia.blueprint.marketing.newsletter;

import java.util.Map;

/**
 * Contains the configuration values set for the Newsletter service.
 */
public interface NewsletterContext {
  String getMarketingConnectionId();

  String getNewsletterId();

  String getMailFromName();

  String getMailFromAddress();

  String getReplyAddress();

  String getDeepLinkTemplate();

  String getLiveCaeUrl();

  String getNewsletterTheme();

  Map<String,Object> getProperties();

  String getProperty(String key);
}
