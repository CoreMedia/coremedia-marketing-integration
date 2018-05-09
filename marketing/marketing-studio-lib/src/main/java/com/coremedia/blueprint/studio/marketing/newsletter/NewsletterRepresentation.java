package com.coremedia.blueprint.studio.marketing.newsletter;

import com.coremedia.blueprint.marketing.newsletter.NewsletterContext;
import com.coremedia.blueprint.studio.marketing.MarketingModel;

import java.net.URI;
import java.util.Map;

public class NewsletterRepresentation {
  private URI pushTemplateUri;

  private String marketingConnectionId;
  private String newsletterTheme;
  private String newsletterId;
  private String deepLinkTemplate;
  private String liveCaeUrl;
  private String replyAddress;
  private String mailFromAddress;
  private String mailFromName;

  private MarketingModel marketing;
  private Map<String,Object> properties;

  public NewsletterRepresentation(NewsletterContext context) {
    this.properties = context.getProperties();
    this.newsletterTheme = context.getNewsletterTheme();

    deepLinkTemplate = context.getDeepLinkTemplate();
    liveCaeUrl = context.getLiveCaeUrl();
    mailFromAddress = context.getMailFromAddress();
    mailFromName = context.getMailFromName();
    replyAddress = context.getReplyAddress();
    newsletterId = context.getNewsletterId();
    marketingConnectionId = context.getMarketingConnectionId();
  }


  public URI getPushTemplateUri() {
    return pushTemplateUri;
  }

  public void setPushTemplateUri(URI pushTemplateUri) {
    this.pushTemplateUri = pushTemplateUri;
  }

  public String getMarketingConnectionId() {
    return marketingConnectionId;
  }

  public void setMarketingConnectionId(String marketingConnectionId) {
    this.marketingConnectionId = marketingConnectionId;
  }

  public String getNewsletterId() {
    return newsletterId;
  }

  public void setNewsletterId(String newsletterId) {
    this.newsletterId = newsletterId;
  }

  public String getDeepLinkTemplate() {
    return deepLinkTemplate;
  }

  public void setDeepLinkTemplate(String deepLinkTemplate) {
    this.deepLinkTemplate = deepLinkTemplate;
  }

  public String getLiveCaeUrl() {
    return liveCaeUrl;
  }

  public void setLiveCaeUrl(String liveCaeUrl) {
    this.liveCaeUrl = liveCaeUrl;
  }

  public String getReplyAddress() {
    return replyAddress;
  }

  public void setReplyAddress(String replyAddress) {
    this.replyAddress = replyAddress;
  }

  public String getMailFromAddress() {
    return mailFromAddress;
  }

  public void setMailFromAddress(String mailFromAddress) {
    this.mailFromAddress = mailFromAddress;
  }

  public String getMailFromName() {
    return mailFromName;
  }

  public void setMailFromName(String mailFromName) {
    this.mailFromName = mailFromName;
  }

  public MarketingModel getMarketing() {
    return marketing;
  }

  public void setMarketing(MarketingModel marketing) {
    this.marketing = marketing;
  }

  public Map<String, Object> getProperties() {
    return properties;
  }

  public String getNewsletterTheme() {
    return newsletterTheme;
  }

  public void setNewsletterTheme(String newsletterTheme) {
    this.newsletterTheme = newsletterTheme;
  }
}
