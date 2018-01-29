package com.coremedia.blueprint.marketing.marketo.api;

import com.coremedia.blueprint.marketing.MarketingContext;
import com.coremedia.blueprint.marketing.marketo.api.rest.MarketoResponse;
import com.coremedia.blueprint.marketing.marketo.api.rest.MarketoStatusMessage;
import com.coremedia.blueprint.marketing.marketo.api.rest.email.MailTemplateResponse;
import com.coremedia.blueprint.marketing.marketo.api.rest.email.MailTemplateResult;
import com.coremedia.blueprint.marketing.marketo.api.rest.folder.FolderResponse;
import com.coremedia.blueprint.marketing.newsletter.NewsletterContext;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 */
public class MarketoClient {
  private static final Logger LOG = LoggerFactory.getLogger(MarketoClient.class);

  private static final String ENDPOINT = "endpoint";
  private static final String CLIENT_ID = "clientId";
  private static final String CLIENT_SECRET = "clientSecret";

  private String tokenUrl;
  private String createEmailTemplateUrl;
  private String updateEmailTemplateUrl;
  private String findFolderUrl;

  public MailTemplateResult updateEmailTemplate(MarketingContext marketingContext, NewsletterContext newsletterContext, String token, String id, String title, String body) throws IOException {
    String endpoint = marketingContext.getProperty(ENDPOINT) + MessageFormat.format(updateEmailTemplateUrl, id, token);
    String boundary = "mktoBoundary" + String.valueOf(System.currentTimeMillis());

    URL url = new URL(endpoint);
    HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
    urlConn.setRequestMethod("POST");
    urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    urlConn.setRequestProperty("accept", "text/json");
    urlConn.setDoOutput(true);
    PrintWriter wr = new PrintWriter(new OutputStreamWriter(urlConn.getOutputStream()));
    //format content as multipart and write it to the output stream
    addMultipart(boundary, body, wr, "content", "text/html", title + ".html");
    closeMultipart(boundary, wr);
    wr.flush();
    int responseCode = urlConn.getResponseCode();
    if (responseCode == 200){
      InputStream inStream = urlConn.getInputStream();
      String responseJson = convertStreamToString(inStream);
      Gson gson = new Gson();
      MailTemplateResponse response = gson.fromJson(responseJson, MailTemplateResponse.class);
      if(!response.isSuccess()) {
        MarketoStatusMessage error = response.getErrors().get(0);
        throw new IOException(error.getMessage());
      }

      if(response.getResult().isEmpty()) {
        throw new IOException("No result document found in REST response.");
      }

      return response.getResult().get(0);
    }

    throw new IOException("Failed to updated mail template '" + id + ", status " + urlConn.getResponseCode());
  }

  public MailTemplateResult creatEmailTemplate(MarketingContext marketingContext, NewsletterContext newsletterContext, String token, String title, String body) throws IOException {
    String boundary = "mktoBoundary" + String.valueOf(System.currentTimeMillis());
    String endpoint = marketingContext.getProperty(ENDPOINT) + MessageFormat.format(createEmailTemplateUrl, token);
    URL url = new URL(endpoint);
    HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
    urlConn.setRequestMethod("POST");
    urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    urlConn.setRequestProperty("accept", "text/json");
    urlConn.setDoOutput(true);

    FolderResponse folder = getFolder(marketingContext, newsletterContext, token);
    if(folder == null) {
      throw new IOException("Cannot find target folder for template");
    }

    String folderJson = new Gson().toJson(folder.getResult().get(0).getFolderId());

    PrintWriter wr = new PrintWriter(new OutputStreamWriter(urlConn.getOutputStream()));

    //format content as multpart and insert into output stream
    addMultipart(boundary, body, wr, "content", "text/html", title + ".html");

    addMultipartData(boundary, title, wr, "name");
    addMultipartData(boundary, folderJson, wr, "folder");
    addMultipartData(boundary, title, wr, "description");

    closeMultipart(boundary, wr);
    wr.flush();
    int responseCode = urlConn.getResponseCode();
    if (responseCode == 200) {
      InputStream inStream = urlConn.getInputStream();
      String result = convertStreamToString(inStream);
      Gson gson = new Gson();
      MailTemplateResponse response = gson.fromJson(result, MailTemplateResponse.class);
      if(!response.isSuccess()) {
        MarketoStatusMessage error = response.getErrors().get(0);
        LOG.error("Error from marketo: " + error.getMessage());
        throw new IOException(error.getMessage());
      }

      if(response.getResult().isEmpty()) {
        throw new IOException("No result document found in REST response.");
      }

      return response.getResult().get(0);
    }

    throw new IOException("Unknown error creating email template: Status " + urlConn.getResponseCode());
  }


  public FolderResponse getFolder(MarketingContext context, NewsletterContext newsletterContext, String token) throws IOException {
    String folderName = (String) newsletterContext.getProperties().get("templateFolder");
    if (folderName == null) {
      LOG.info("No Marketo email template folder configured, using 'Templates' as fallback");
      folderName = "Templates";
    }

    String findFolderUrlTemplate = context.getProperty(ENDPOINT) + findFolderUrl;
    String url = MessageFormat.format(findFolderUrlTemplate, folderName, token);
    String jsonString = getResponseJson(url);

    Gson gson = new Gson();
    MarketoResponse statusMessage = gson.fromJson(jsonString, MarketoResponse.class);
    if (statusMessage.isSuccess()) {
      if (statusMessage.getWarnings() != null && !statusMessage.getWarnings().isEmpty()) {
        throw new IOException(statusMessage.getWarnings().get(0));
      }

      FolderResponse folderResponse = gson.fromJson(jsonString, FolderResponse.class);
      if (folderResponse.getResult().isEmpty()) {
        return null;
      }

      return folderResponse;
    }

    throw new IOException(statusMessage.getErrors().get(0).getMessage());
  }

  public String getToken(MarketingContext context) throws IOException {
    String clientId = context.getProperty(CLIENT_ID);
    String clientSecret = context.getProperty(CLIENT_SECRET);
    String endPoint = context.getProperty(ENDPOINT) + MessageFormat.format(tokenUrl, clientId, clientSecret);

    String jsonString = getResponseJson(endPoint);
    Gson gson = new Gson();
    MarketoResponse statusMessage = gson.fromJson(jsonString, MarketoResponse.class);

    if (statusMessage.getErrors() != null && !statusMessage.getErrors().isEmpty()) {
      throw new IOException(statusMessage.getErrors().get(0).getMessage());
    }

    JsonParser jsonParser = new JsonParser();
    JsonObject jo = (JsonObject) jsonParser.parse(jsonString);
    return jo.get("access_token").getAsString();
  }

  // -------------------- Helper----------------------------------------------------------------------------------------

  private String getResponseJson(String urlString) throws IOException {
    URL url = new URL(urlString);
    HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
    urlConn.setRequestMethod("GET");
    urlConn.setRequestProperty("accept", "application/json");
    int responseCode = urlConn.getResponseCode();
    if (responseCode == 200) {
      InputStream inStream = urlConn.getInputStream();
      return IOUtils.toString(inStream, "utf8");
    }

    throw new IOException("Unable to execute marketo REST request, status " + urlConn.getResponseMessage());
  }

  //Add content as multipart form-data
  private void addMultipart(String boundary, String requestBody,
                            PrintWriter wr, String paramName, String contentType, String file) {
    wr.append("--" + boundary + "\r\n");
    wr.append("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + file + "\"");
    wr.append("\r\n");
    wr.append("Content-type: " + contentType + "; charset=\"utf-8\"\r\n");
    wr.append("\r\n");
    wr.append(requestBody);
    wr.append("\r\n");
  }


  //Add content as multipart form-data
  private void addMultipartData(String boundary, String requestBody,
                                PrintWriter wr, String paramName) {
    wr.append("--" + boundary + "\r\n");
    wr.append("Content-Disposition: form-data; name=\"" + paramName + "\"");
    wr.append("\r\n");
    wr.append("\r\n");
    wr.append(requestBody);
    wr.append("\r\n");
  }

  //close multipart content
  private void closeMultipart(String boundary, PrintWriter wr) {
    wr.append("--");
    wr.append(boundary);
    wr.append("--");
  }


  private String convertStreamToString(InputStream inputStream) {
    try {
      return new Scanner(inputStream).useDelimiter("\\A").next();
    } catch (NoSuchElementException e) {
      return "";
    }
  }

  //----------------- Spring -------------------------------------------------------------------------------------------

  @Required
  public void setTokenUrl(String tokenUrl) {
    this.tokenUrl = tokenUrl;
  }

  @Required
  public void setCreateEmailTemplateUrl(String createEmailTemplateUrl) {
    this.createEmailTemplateUrl = createEmailTemplateUrl;
  }

  @Required
  public void setFindFolderUrl(String findFolderUrl) {
    this.findFolderUrl = findFolderUrl;
  }

  @Required
  public void setUpdateEmailTemplateUrl(String updateEmailTemplateUrl) {
    this.updateEmailTemplateUrl = updateEmailTemplateUrl;
  }
}
