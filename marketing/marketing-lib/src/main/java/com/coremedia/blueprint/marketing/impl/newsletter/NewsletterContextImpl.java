package com.coremedia.blueprint.marketing.impl.newsletter;

import com.coremedia.blueprint.marketing.newsletter.NewsletterContext;

import java.util.Map;

public class NewsletterContextImpl implements NewsletterContext {
  private final static String MARKETING_CONNECTION_ID = "marketingConnectionId";
  private final static String NEWSLETTER_ID = "newsletterId";
  private final static String NEWSLETTER_THEME = "newsletterTheme";
  private final static String MAIL_FROM_NAME = "mailFromName";
  private final static String MAIL_FROM_ADRESS = "mailFromAddress";
  private final static String REPLY_TO_ADRESS = "mailReplyTo";
  private final static String DEEP_LINK_TEMPLATE = "deepLinkTemplate";
  private final static String LIVE_CAE_URL = "liveCaeUrl";

  private Map<String, Object> properties;

  NewsletterContextImpl(Map<String, Object> properties) {
    this.properties = properties;
  }

  public Map<String,Object> getProperties() {
    return properties;
  }

  @Override
  public String getProperty(String key) {
    return (String) properties.get(key);
  }

  @Override
  public String getMarketingConnectionId() {
    return (String)properties.get(MARKETING_CONNECTION_ID);
  }

  @Override
  public String getNewsletterId() {
    return (String)properties.get(NEWSLETTER_ID);
  }

  @Override
  public String getMailFromName() {
    return (String)properties.get(MAIL_FROM_NAME);
  }

  @Override
  public String getMailFromAddress() {
    return (String)properties.get(MAIL_FROM_ADRESS);
  }

  @Override
  public String getReplyAddress() {
    return (String)properties.get(REPLY_TO_ADRESS);
  }

  @Override
  public String getDeepLinkTemplate() {
    return (String)properties.get(DEEP_LINK_TEMPLATE);
  }

  @Override
  public String getLiveCaeUrl() {
    return (String)properties.get(LIVE_CAE_URL);
  }

  @Override
  public String getNewsletterTheme() {
    return (String)properties.get(NEWSLETTER_THEME);
  }
}
