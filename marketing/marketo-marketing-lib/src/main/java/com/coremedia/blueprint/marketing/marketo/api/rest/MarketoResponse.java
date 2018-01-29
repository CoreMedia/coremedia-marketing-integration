package com.coremedia.blueprint.marketing.marketo.api.rest;

import java.util.List;

/**
 * {"requestId":"99c1#160453ebe60","success":false,"errors":[{"code":"600","message":"Access token not specified"}]}
 */
public class MarketoResponse {
  private String requestId;
  private boolean success;
  private List<MarketoStatusMessage> errors;
  private List<String> warnings;

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public List<MarketoStatusMessage> getErrors() {
    return errors;
  }

  public void setErrors(List<MarketoStatusMessage> errors) {
    this.errors = errors;
  }

  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }
}
