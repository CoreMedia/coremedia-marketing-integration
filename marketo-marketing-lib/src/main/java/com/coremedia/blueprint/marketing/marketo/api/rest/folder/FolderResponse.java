package com.coremedia.blueprint.marketing.marketo.api.rest.folder;

import com.coremedia.blueprint.marketing.marketo.api.rest.MarketoResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FolderResponse extends MarketoResponse {
  private List<FolderResult> result = new ArrayList<>();

  public List<FolderResult> getResult() {
    return result;
  }

  public void setResult(List<FolderResult> result) {
    this.result = result;
  }
}
