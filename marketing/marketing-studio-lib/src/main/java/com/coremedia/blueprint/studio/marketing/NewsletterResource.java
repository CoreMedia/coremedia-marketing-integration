package com.coremedia.blueprint.studio.marketing;

import com.coremedia.blueprint.base.links.ContentLinkBuilder;
import com.coremedia.blueprint.marketing.MarketingActionResponse;
import com.coremedia.blueprint.marketing.MarketingConnection;
import com.coremedia.blueprint.marketing.MarketingContext;
import com.coremedia.blueprint.marketing.impl.Marketing;
import com.coremedia.blueprint.marketing.impl.MarketingContextProvider;
import com.coremedia.blueprint.marketing.impl.newsletter.NewsletterContextProvider;
import com.coremedia.blueprint.marketing.newsletter.NewsletterContext;
import com.coremedia.blueprint.studio.marketing.model.MarketingModel;
import com.coremedia.blueprint.studio.marketing.representation.NewsletterRepresentation;
import com.coremedia.cap.common.IdHelper;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.rest.linking.AbstractLinkingResource;
import com.coremedia.rest.linking.EntityResource;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.tika.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.util.UriComponentsBuilder;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Date;

import static com.coremedia.blueprint.studio.marketing.NewsletterResource.NEWSLETTER_ID;

@Produces(MediaType.APPLICATION_JSON)
@Path("marketing/newsletter/{" + NEWSLETTER_ID + ":[^/]+}")
public class NewsletterResource extends AbstractLinkingResource implements EntityResource<NewsletterContext> {
  private static final Logger LOG = LoggerFactory.getLogger(NewsletterResource.class);

  private static final String ABSOLUTE_URI_KEY = "absolute";
  static final String NEWSLETTER_ID = "newsletterId";

  private ContentRepository contentRepository;
  private NewsletterContextProvider newsletterContextProvider;
  private Marketing marketing;
  private MarketingContextProvider marketingContextProvider;
  private String newsletterId;


  private ContentLinkBuilder contentLinkBuilder;

  @PathParam(NEWSLETTER_ID)
  public void setNewsletterId(final String newsletterId) {
    this.newsletterId = newsletterId;
  }

  @GET
  public NewsletterRepresentation get() throws Exception {
    NewsletterContext newsletterContext = getEntity();
    if (getEntity() == null) {
      return null;
    }
    NewsletterRepresentation representation = new NewsletterRepresentation(newsletterContext);

    MarketingConnection connection = getConnection();
    representation.setMarketing(new MarketingModel(connection));

    String self = "/api/marketing/newsletter/" + newsletterId;
    representation.setPushTemplateUri(new URI(self + "/push"));

    return representation;
  }

  @Override
  public NewsletterContext getEntity() {
    return newsletterContextProvider.createContext(newsletterId);
  }

  @Override
  public void setEntity(NewsletterContext entity) {
    //not used
  }

  @POST
  @Path("push")
  @Produces(MediaType.APPLICATION_JSON)
  public MarketingActionResponse pushTemplate(@FormParam("contentId") int contentId,
                                              @FormParam("templateId") String templateId) {
    try {
      String capId = IdHelper.formatContentId(contentId);
      Content content = contentRepository.getContent(capId);
      MarketingContext marketingContext = getConnection().getMarketingContext();
      NewsletterContext newsletterContext = getEntity();
      String title = content.getString("title");
      String body = getHtmlBody(newsletterContext, content);
      if (body != null) {
        return getConnection().getNewsletterService().pushTemplate(marketingContext, newsletterContext, templateId, title, body);
      }

      String url = getTemplateUrl(newsletterContext, content).build().toString();
      return new MarketingActionResponse(false, null,
              "Failed to send template to marketing service since the CAE did not return a valid template.\nTemplate URL: " + url, new Date());
    } catch (Exception e) {
      LOG.error("Failed to send template to marketing service: " + e.getMessage(), e);
      return new MarketingActionResponse(false, null,
              "Failed to send template to marketing service: " + e.getMessage(), new Date());
    }
  }

  // ---------------------- Helper -------------------------------------------------------------------------------------

  private class InternalAcceptAllTrustStrategy implements TrustStrategy {
    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) {
      return true;
    }
  }

  /**
   * Returns the HTML template that is used for the newsletter.
   * We use the CAE to generate the template from the given URL.
   */
  private String getHtmlBody(NewsletterContext newsletterContext, Content mailTemplateContent) throws URISyntaxException {
    UriComponentsBuilder uriComponentsBuilder = getTemplateUrl(newsletterContext, mailTemplateContent);
    String previewUrl = uriComponentsBuilder.build().toString();
    LOG.info("Creating preview template from URL " + previewUrl);

    SSLContextBuilder builder = new SSLContextBuilder();
    try {
      LOG.info("Loading newsletter content from URL:" + previewUrl);
      builder.loadTrustMaterial(null, new InternalAcceptAllTrustStrategy());

      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
      CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
      HttpGet httpGet = new HttpGet(previewUrl);
      CloseableHttpResponse httpResponse = httpclient.execute(httpGet);

      int statusCode = httpResponse.getStatusLine().getStatusCode();
      if (statusCode == 200) {
        InputStream content = httpResponse.getEntity().getContent();

        StringWriter writer = new StringWriter();
        IOUtils.copy(content, writer);
        return writer.toString();
      }
      else {
        LOG.error("Failed to load mail template from Live CAE: " + statusCode);
      }
    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
      LOG.error("Failed to load html body for newsletter.", e);
    }
    return null;
  }

  private UriComponentsBuilder getTemplateUrl(NewsletterContext newsletterContext, Content mailTemplateContent) throws URISyntaxException {
    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(new URI(newsletterContext.getLiveCaeUrl()));
    uriComponentsBuilder.path(contentLinkBuilder.buildLinkForLinkable(mailTemplateContent).build().getPath());
    uriComponentsBuilder.queryParam(ABSOLUTE_URI_KEY, true);
    return uriComponentsBuilder;
  }

  private MarketingConnection getConnection() {
    String marketingConnectionId = getEntity().getMarketingConnectionId();
    MarketingContext marketingContext = marketingContextProvider.findContext(marketingConnectionId);
    if (marketingContext == null) {
      LOG.error("NewsletterResource could not find a marketing context for the connection id '"
              + marketingConnectionId + "', check NewsletterSettings property 'marketingConnectionId'.");
    }
    return marketing.getConnection(marketingContext);
  }

  //------------------------ Spring-------------------------------------------------------------------------------------

  @Required
  public void setContentRepository(ContentRepository contentRepository) {
    this.contentRepository = contentRepository;
  }

  @Required
  public void setNewsletterContextProvider(NewsletterContextProvider newsletterContextProvider) {
    this.newsletterContextProvider = newsletterContextProvider;
  }

  @Required
  public void setMarketing(Marketing marketing) {
    this.marketing = marketing;
  }

  @Required
  public void setMarketingContextProvider(MarketingContextProvider marketingContextProvider) {
    this.marketingContextProvider = marketingContextProvider;
  }

  @Required
  public void setContentLinkBuilder(ContentLinkBuilder contentLinkBuilder) {
    this.contentLinkBuilder = contentLinkBuilder;
  }
}
