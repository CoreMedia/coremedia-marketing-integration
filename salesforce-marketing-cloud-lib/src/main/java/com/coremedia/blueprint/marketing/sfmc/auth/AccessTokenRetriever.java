package com.coremedia.blueprint.marketing.sfmc.auth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;


public class AccessTokenRetriever {
  private static final Logger LOG = LoggerFactory.getLogger(AccessTokenRetriever.class);

  private static final String PARAM_CLIENT_ID = "clientId";
  private static final String PARAM_CLIENT_SECRET = "clientSecret";

  private String url;
  private HttpClient httpClient;
  private String responseText;
  private AccessToken lastToken;

  public AccessTokenRetriever(String url) {
    this.url = url;
    httpClient = HttpClientBuilder.create().build();
  }

  public String retrieveToken(String clientId, String clientSecret) {
    try {
      if(lastToken != null && lastToken.isValid()) {
        return lastToken.getToken();
      }

      HttpPost post = createPost(clientId, clientSecret);
      HttpResponse execute = httpClient.execute(post);
      responseText = getResponseText(execute);
      lastToken = getTokenFromResponse();
      return lastToken.getToken();
    } catch (Exception e) {
      LOG.error("Error retrieving token from ", e);
      throw new RuntimeException(e);
    }
  }

  private HttpPost createPost(String clientId, String clientSecret) {
    HttpPost httpPost = new HttpPost(url);

    String json = "{\n" +
            "    \"clientId\": \"" + clientId + "\",\n" +
            "    \"clientSecret\": \"" + clientSecret + "\"\n" +
            "}";
    httpPost.setEntity(new ByteArrayEntity(json.getBytes(), ContentType.APPLICATION_JSON));

    int connectionTimeout = 10 * 1000;
    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(connectionTimeout)
            .setConnectTimeout(connectionTimeout)
            .setSocketTimeout(connectionTimeout)
            .build();
    httpPost.setConfig(requestConfig);

    return httpPost;
  }

  private String getResponseText(HttpResponse response) throws IOException {
    String result;
    HttpEntity entity = response.getEntity();
    try {
      Scanner scanner = new Scanner(entity.getContent()).useDelimiter("\\A");
      result = scanner.hasNext() ? scanner.next() : "";
    } finally {
      EntityUtils.consume(entity);
    }

    return result;
  }

  private AccessToken getTokenFromResponse() {
    JSONTokener tokener = new JSONTokener(responseText);
    JSONObject json = new JSONObject(tokener);
    String token = json.getString("accessToken");
    long expires = Long.parseLong(json.getString("expiresIn"));
    return new AccessToken(token, expires);
  }


}
