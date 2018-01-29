package com.coremedia.blueprint.marketing.ibm.api;

import com.coremedia.blueprint.marketing.MarketingActionResponse;
import com.coremedia.blueprint.marketing.MarketingContext;
import com.coremedia.blueprint.marketing.ibm.IbmDigitalMarketingApiResponse;
import com.coremedia.blueprint.marketing.ibm.MarketingCloudClientImpl;
import com.coremedia.blueprint.marketing.ibm.mailing.MailingTemplate;
import com.coremedia.blueprint.marketing.ibm.mailing.SaveMailingCommandResponse;
import com.coremedia.blueprint.marketing.newsletter.NewsletterContext;
import com.coremedia.blueprint.marketing.newsletter.NewsletterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Newsletter Service for IBM
 */
public class IBMNewsletterServiceImpl implements NewsletterService {
  private static final Logger LOG = LoggerFactory.getLogger(IBMNewsletterServiceImpl.class);

  private static final int VISIBILITY = 1;
  private static final String FOLDER_PATH = "folderPath";

  @Override
  public MarketingActionResponse pushTemplate(MarketingContext marketingContext, NewsletterContext context, String templateId, String title, String htmlBody) {
    try {
      String mailingId = "";
      String fromName = context.getMailFromName();
      String fromAddress = context.getMailFromAddress();
      String replyTo = context.getReplyAddress();
      String folderPath = (String) context.getProperties().get(FOLDER_PATH);


      MarketingCloudClientImpl marketingCloudClient = getMarketingCloudClient(marketingContext);
      IbmDigitalMarketingApiResponse apiResponse = marketingCloudClient.createOrUpdateMailingTemplate(title, mailingId, fromName, fromAddress, replyTo, title, htmlBody, folderPath, VISIBILITY);

      mailingId = ((SaveMailingCommandResponse) apiResponse).getMailingId();

      if (mailingId != null && mailingId.length() > 0) {
        MailingTemplate mailingTemplate = marketingCloudClient.getMailingTemplate(mailingId, VISIBILITY);
        Date lastModified = mailingTemplate.getLastModified();

        return new MarketingActionResponse(apiResponse.isSuccess(), mailingId, title, lastModified);
      }
      else {
        String faultString = ((SaveMailingCommandResponse) apiResponse).getFaultString();
        LOG.error("Could not create mailing '" + title + "' template. API Result:" + faultString);
        return new MarketingActionResponse(false, null, "Failed to create mailing template '" + title + "': " + faultString, new Date());
      }
    } catch (Exception e) {
      LOG.error("Failed to push IBM newsletter template: " + e.getMessage(), e);
      return new MarketingActionResponse(false, null, e.getMessage(), new Date());
    }
  }


  /**
   * Creates the client connection using the marketing context information
   */
  private MarketingCloudClientImpl getMarketingCloudClient(MarketingContext marketingContext) {
    String url = marketingContext.getProperty("url");
    String clientId = marketingContext.getProperty("clientId");
    String clientSecret = marketingContext.getProperty("clientSecret");
    String refreshToken = marketingContext.getProperty("refreshToken");

    if (StringUtils.isEmpty(url) ||
            StringUtils.isEmpty(clientId) ||
            StringUtils.isEmpty(clientSecret) ||
            StringUtils.isEmpty(refreshToken)) {
      throw new UnsupportedOperationException("Invalid IBM connection, checks MarketingSettings properties.");
    }

    return new MarketingCloudClientImpl(url, clientId, clientSecret, refreshToken, 10);
  }
}
