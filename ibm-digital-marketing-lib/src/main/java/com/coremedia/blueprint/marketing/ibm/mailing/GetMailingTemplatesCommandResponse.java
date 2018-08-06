package com.coremedia.blueprint.marketing.ibm.mailing;


import com.coremedia.blueprint.marketing.ibm.IbmDigitalMarketingApiResponse;

import java.util.List;

public class GetMailingTemplatesCommandResponse extends IbmDigitalMarketingApiResponse {
  private List<MailingTemplate> mailingTemplates;

  GetMailingTemplatesCommandResponse(Boolean isSuccess) {
    super(isSuccess);
  }

  public List<MailingTemplate> getMailingTemplates() {
    return mailingTemplates;
  }

  public void setMailingTemplates(List<MailingTemplate> mailingTemplates) {
    this.mailingTemplates = mailingTemplates;
  }
}
