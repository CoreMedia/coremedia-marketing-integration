package com.coremedia.blueprint.marketing.marketo.api;


import com.coremedia.blueprint.marketing.MarketingActionResponse;
import com.coremedia.blueprint.marketing.MarketingContext;
import com.coremedia.blueprint.marketing.marketo.api.rest.email.MailTemplateResult;
import com.coremedia.blueprint.marketing.newsletter.NewsletterContext;
import com.coremedia.blueprint.marketing.newsletter.NewsletterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;

import java.util.Date;

public class MarketoNewsletterServiceImpl implements NewsletterService {
  private static final Logger LOG = LoggerFactory.getLogger(MarketoNewsletterServiceImpl.class);

  private MarketoClient client;

  @Override
  public MarketingActionResponse pushTemplate(MarketingContext marketingContext, NewsletterContext newsletterContext, String templateId, String title, String body) {
    try {
      String token = client.getToken(marketingContext);
      MailTemplateResult createTemplateResult = null;

      //the template is created or updated, depending on an existing templateId
      if (StringUtils.isEmpty(templateId)) {
        try {
          createTemplateResult = client.creatEmailTemplate(marketingContext, newsletterContext, token, title, body);
        }
        catch (Exception e) {
          String msg = e.getMessage();
          if(msg != null && msg.toLowerCase().contains("duplicate")) {
            LOG.warn("An existing template was already found with name '" + title + "', executing update instead.");
            createTemplateResult = client.updateEmailTemplate(marketingContext, newsletterContext, token, templateId, title, body);
          }
        }
      }
      else {
        createTemplateResult = client.updateEmailTemplate(marketingContext, newsletterContext, token, templateId, title, body);
      }

      return new MarketingActionResponse(true, String.valueOf(createTemplateResult.getId()), createTemplateResult.getDescription(), new Date());
    } catch (Exception e) {
      LOG.error("Failed to push marketo newsletter template: " + e.getMessage(), e);
      return new MarketingActionResponse(false, null, e.getMessage(), new Date());
    }
  }

  @Required
  public void setClient(MarketoClient client) {
    this.client = client;
  }
}
