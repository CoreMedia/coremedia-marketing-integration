package com.coremedia.blueprint.marketing.ibm;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;


public class AccessTokenRetriever {
  private static final Logger LOG = LoggerFactory.getLogger(AccessTokenRetriever.class);

  private static final String PARAM_CLIENT_ID = "client_id";
  private static final String PARAM_CLIENT_SECRET = "client_secret";
  private static final String PARAM_REFRESH_TOKEN = "refresh_token";
  private static final String PARAM_GRANT_TYPE = "grant_type";
  private static final String GRANT_TYPE = "refresh_token";

  private String url;
  private HttpClient httpClient;
  private String responseText;

  AccessTokenRetriever(String url) {
    this.url = url + "/oauth/token";
    httpClient = HttpClientBuilder.create().build();
  }

  public String retrieveToken(String clientId, String clientSecret, String refereshToken) {
    try {
      HttpPost post = createPost(clientId, clientSecret, refereshToken);
      HttpResponse execute = httpClient.execute(post);
      responseText = getResponseText(execute);
      return getTokenFromResponse();
    } catch (Exception e) {
      LOG.error("Error retrieving token from ", e);
      throw new RuntimeException(e);
    }
  }

  private HttpPost createPost(String clientId, String clientSecret, String refereshToken) throws UnsupportedEncodingException {
    HttpPost httpPost = new HttpPost(url);

    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    postParameters.add(new BasicNameValuePair(PARAM_CLIENT_ID, clientId));
    postParameters.add(new BasicNameValuePair(PARAM_CLIENT_SECRET, clientSecret));
    postParameters.add(new BasicNameValuePair(PARAM_REFRESH_TOKEN, refereshToken));
    postParameters.add(new BasicNameValuePair(PARAM_GRANT_TYPE, GRANT_TYPE));

    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(postParameters);
    httpPost.setEntity(urlEncodedFormEntity);

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

  private String getTokenFromResponse() {
    JSONTokener tokener = new JSONTokener(responseText);
    JSONObject json = new JSONObject(tokener);
    return json.getString("access_token");
  }
}
