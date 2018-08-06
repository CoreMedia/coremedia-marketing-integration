package com.coremedia.blueprint.marketing.ibm.mailing;


import com.coremedia.blueprint.marketing.ibm.IbmDigitalMarketingApiResponse;

public class SaveMailingCommandResponse extends IbmDigitalMarketingApiResponse {
  private String mailingId = "";
  private String faultString = "";

  SaveMailingCommandResponse(boolean isSuccess) {
    super(isSuccess);
  }

  public void setMailingId(String mailingId) {
    this.mailingId = mailingId;
  }

  public String getMailingId() {
    return mailingId;
  }

  public String getFaultString() {
    return faultString;
  }

  public void setFaultString(String faultString) {
    this.faultString = faultString;
  }
}
