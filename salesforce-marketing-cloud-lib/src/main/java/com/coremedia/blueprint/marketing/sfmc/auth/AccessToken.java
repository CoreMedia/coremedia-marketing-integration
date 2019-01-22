package com.coremedia.blueprint.marketing.sfmc.auth;

import java.util.Date;

/**
 *
 */
public class AccessToken {
  private String token;
  private long maxValiditySeconds;
  private long creationTimeSeconds;

  public AccessToken(String token, long maxValiditySeconds) {
    this.token = token;
    this.creationTimeSeconds = new Date().getTime() / 1000;
    this.maxValiditySeconds = maxValiditySeconds;
  }

  public boolean isValid() {
    long livingSinceSeconds = new Date().getTime()/1000-creationTimeSeconds;
    return livingSinceSeconds < maxValiditySeconds;
  }

  public String getToken() {
    return token;
  }
}
