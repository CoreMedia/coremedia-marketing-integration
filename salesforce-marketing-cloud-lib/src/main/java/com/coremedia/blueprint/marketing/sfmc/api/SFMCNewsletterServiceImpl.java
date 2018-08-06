package com.coremedia.blueprint.marketing.sfmc.api;

import com.coremedia.blueprint.marketing.MarketingActionResponse;
import com.coremedia.blueprint.marketing.MarketingContext;
import com.coremedia.blueprint.marketing.newsletter.NewsletterContext;
import com.coremedia.blueprint.marketing.newsletter.NewsletterService;
import com.exacttarget.fuelsdk.ETClient;
import com.exacttarget.fuelsdk.ETConfiguration;
import com.exacttarget.fuelsdk.ETEmail;
import com.exacttarget.fuelsdk.ETResponse;
import com.exacttarget.fuelsdk.ETResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

public class SFMCNewsletterServiceImpl implements NewsletterService {
  private static final Logger LOG = LoggerFactory.getLogger(SFMCNewsletterServiceImpl.class);

  @Override
  public MarketingActionResponse pushTemplate(MarketingContext marketingContext, NewsletterContext context, String templateId, String title, String body) {
    try {
      ETEmail email = new ETEmail();
      email.setName(title);
      email.setKey(UUID.randomUUID().toString());
      email.setHtmlBody(body);
      email.setTextBody(body);
      email.setSubject(title);

      ETClient client = getClient(marketingContext);
      ETResponse<ETEmail> response = client.create(email);
      ETResult<ETEmail> result = response.getResult();

      String responseMessage = result.getResponseMessage();
      String key = result.getObject().getKey();
      return new MarketingActionResponse(true, key, responseMessage, new Date());
    } catch (Exception e) {
      LOG.error("Failed to push SFMC newsletter template: " + e.getMessage(), e);
      return new MarketingActionResponse(false, null, e.getMessage(), new Date());
    }
  }

  /**
   * Creates the client connection using the marketing context information
   */
  private ETClient getClient(MarketingContext marketingContext) throws Exception {
    String clientId = marketingContext.getProperty("clientId");
    String clientSecret = marketingContext.getProperty("clientSecret");

    if (StringUtils.isEmpty(clientId) ||
            StringUtils.isEmpty(clientSecret)) {
      throw new UnsupportedOperationException("Invalid SFMC connection, checks MarketingSettings properties.");
    }

    ETConfiguration config = new ETConfiguration();
    config.set("clientId", clientId);
    config.set("clientSecret", clientSecret);
    return new ETClient(config);
  }

}
