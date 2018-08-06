package com.coremedia.blueprint.marketing.marketo.api.rest.email;

import com.coremedia.blueprint.marketing.marketo.api.rest.MarketoResponse;

import java.util.ArrayList;
import java.util.List;

public class MailTemplateResponse extends MarketoResponse {
  private List<MailTemplateResult> result = new ArrayList<>();

  public List<MailTemplateResult> getResult() {
    return result;
  }

  public void setResult(List<MailTemplateResult> result) {
    this.result = result;
  }
}
