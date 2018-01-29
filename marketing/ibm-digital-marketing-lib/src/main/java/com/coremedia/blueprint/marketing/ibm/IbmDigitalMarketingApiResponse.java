package com.coremedia.blueprint.marketing.ibm;

public class IbmDigitalMarketingApiResponse {

  private boolean isSuccess;

  public IbmDigitalMarketingApiResponse(boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

}
