package com.coremedia.blueprint.marketing;

import java.util.Date;

/**
 * REST response POJO used for common error handling of marketing services.
 */
public class MarketingActionResponse {
  private String id;
  private String result;
  private Date lastModified;
  private boolean success;

  public MarketingActionResponse(boolean success, String id, String result, Date lastModified) {
    this.success = success;
    this.id = id;
    this.result = result;
    this.lastModified = lastModified;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getId() {
    return id;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public String getResult() {
    return result;
  }
}
