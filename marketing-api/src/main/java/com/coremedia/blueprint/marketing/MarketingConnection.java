package com.coremedia.blueprint.marketing;

import com.coremedia.blueprint.marketing.newsletter.NewsletterService;
import org.springframework.beans.factory.BeanNameAware;

/**
 * Instances of this class are created for new marketing connections.
 */
public class MarketingConnection implements BeanNameAware {
  private String type;

  private MarketingContext marketingContext;

  private NewsletterService newsletterService;

  public void setNewsletterService(NewsletterService newsletterService) {
    this.newsletterService = newsletterService;
  }

  public NewsletterService getNewsletterService() {
    return newsletterService;
  }

  public MarketingContext getMarketingContext() {
    return marketingContext;
  }

  public void setMarketingContext(MarketingContext marketingContext) {
    this.marketingContext = marketingContext;
  }

  @Override
  public void setBeanName(String s) {
    this.type = s;
  }

  public String getType() {
    if(type.contains(":")) {
      return type.split(":")[1];
    }
    return type;
  }
}
