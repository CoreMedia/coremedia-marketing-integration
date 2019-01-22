package com.coremedia.blueprint.marketing.ibm;

import com.coremedia.blueprint.marketing.ibm.mailing.GetMailingTemplatesCommand;
import com.coremedia.blueprint.marketing.ibm.mailing.GetMailingTemplatesCommandResponse;
import com.coremedia.blueprint.marketing.ibm.mailing.MailingTemplate;
import com.coremedia.blueprint.marketing.ibm.mailing.SaveMailingCommand;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MarketingCloudClientImpl implements MarketingCloudClient {
  private static final Logger LOG = LoggerFactory.getLogger(MarketingCloudClientImpl.class);

  private String apiUrl;
  private String refreshToken;
  private String clientSecret;
  private String clientId;
  private String accessToken;
  private final int connectionTimeout;

  public MarketingCloudClientImpl(String url, String clientId, String clientSecret, String refreshToken, int connectionTimeout) {
    this.refreshToken = refreshToken;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.apiUrl = url;
    this.connectionTimeout = connectionTimeout * 1000; // Timeout in millis.
  }

  @Override
  public IbmDigitalMarketingApiResponse createOrUpdateMailingTemplate(String mailingName, String mailingId, String fromName, String fromAddress, String replyTo, String subject, String htmlBody, String folderPath, int visibility) {
    LOG.info("Creating or updating mailing. Mailing Name:" + mailingName + " ...");

    //maybe the template has already been pushed, but we haven't stored the templateId for some reason
    if(mailingId == null || mailingId.equals("")) {
      //in that case, search for the template
      List<MailingTemplate> mailingTemplates = getMailingTemplates(visibility);
      for (MailingTemplate mailingTemplate : mailingTemplates) {
        if(mailingTemplate.getName().equals(mailingName)) {
          //and determine the existing id to perform an update
          mailingId = mailingTemplate.getId();
          break;
        }
      }
    }

    SaveMailingCommand mailingCommand = new SaveMailingCommand(mailingName, mailingId, fromName, fromAddress, replyTo, subject, htmlBody, folderPath, visibility);
    return processRequestInternal(mailingCommand);
  }

  @Override
  public List<MailingTemplate> getMailingTemplates(int visibility) {
    LOG.info("Getting Mailing Templates...");
    GetMailingTemplatesCommand command = new GetMailingTemplatesCommand(visibility);
    GetMailingTemplatesCommandResponse ibmDigitalMarketingApiResponse = (GetMailingTemplatesCommandResponse) processRequestInternal(command);
    return ibmDigitalMarketingApiResponse.getMailingTemplates();
  }

  @Override
  public MailingTemplate getMailingTemplate(String mailingId, int visibility) {
    List<MailingTemplate> mailingTemplates = getMailingTemplates(visibility);
    MailingTemplate response = null;
    if (mailingTemplates != null) {
      for (MailingTemplate template : mailingTemplates) {
        if (template.getId().equals(mailingId)) {
          response = template;
          break;
        }
      }
    }
    return response;
  }

  private IbmDigitalMarketingApiResponse processRequestInternal(IbmDigitalMarketingApiCommand command) {
    IbmDigitalMarketingApiResponse response = null;
    String request = command.toString();
    String sendEncoding = "utf-8";
    OutputStream out = null;
    InputStream in = null;

    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(connectionTimeout)
            .setConnectTimeout(connectionTimeout)
            .setSocketTimeout(connectionTimeout)
            .build();

    try {
      if (accessToken == null) {
        LOG.info("Not logged in. Trying to get access token...");
        getAccessToken();
      }
      {
        LOG.info("Using access token: " + accessToken);
      }

      HttpPost httpPost = new HttpPost(apiUrl + "/XMLAPI");
      httpPost.setConfig(requestConfig);
      httpPost.addHeader("Authorization", "Bearer " + accessToken);
      httpPost.addHeader("Content-Type", "text/xml;charset=" + sendEncoding);
      httpPost.setEntity(new StringEntity(request, sendEncoding));

      CloseableHttpClient client = HttpClientBuilder.create().build();

      CloseableHttpResponse postResponse = null;
      postResponse = client.execute(httpPost);
      LOG.info(postResponse.getStatusLine().toString());

      try {
        HttpEntity entity = postResponse.getEntity();
        response = command.postProcessResponseInternal(entity.getContent());
        EntityUtils.consume(entity);
      } finally {
        postResponse.close();
      }


    } catch (IOException e) {
      LOG.error("Error sending post request to :" + apiUrl, e);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (Exception e) {
        }
      }
      if (in != null) {
        try {
          in.close();
        } catch (Exception e) {
        }
      }

    }
    return response;
  }


  private void getAccessToken() {
    AccessTokenRetriever tokenRetriever = new AccessTokenRetriever(apiUrl + "/oauth/token");
    accessToken = tokenRetriever.retrieveToken(clientId, clientSecret, refreshToken);
    LOG.info("Retrieved accessToken: " + accessToken);
  }
}
