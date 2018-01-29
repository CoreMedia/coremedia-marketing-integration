package com.coremedia.blueprint.marketing.ibm;


import com.coremedia.blueprint.marketing.ibm.mailing.MailingTemplate;

import java.util.List;

public interface MarketingCloudClient {
  String XML_API_DATE_FORMAT = "MM/dd/yy hh:mm a";

  IbmDigitalMarketingApiResponse createOrUpdateMailingTemplate(String mailingName, String mailingId, String fromName, String fromAddress, String replyTo, String subject, String htmlBody, String folderPath, int visibility);

  List<MailingTemplate> getMailingTemplates(int visibility);

  MailingTemplate getMailingTemplate(String mailingName, int visibility);
}
