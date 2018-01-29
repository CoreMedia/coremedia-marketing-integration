package com.coremedia.blueprint.marketing.ibm.mailing;

import com.coremedia.blueprint.marketing.ibm.IbmDigitalMarketingApiCommand;
import com.coremedia.blueprint.marketing.ibm.IbmDigitalMarketingApiResponse;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import java.util.Objects;

public class SaveMailingCommand extends IbmDigitalMarketingApiCommand {
  private static final String XPATH_MAILING_ID = "/Envelope/Body/RESULT/MailingID";
  private static final String XPATH_FAULT_STRING = "/Envelope/Body/Fault/FaultString";

  private static String saveOrUpdateMailTemplateRequest = "<Envelope>" +
          "<Body>" +
          "<SaveMailing>" +
          "<Header>" +
          "<MailingName>%%MailingName%%</MailingName>" +
          "<MailingID>%%MailingId%%</MailingID>" +
          "<FromName>%%FromName%%</FromName>" +
          "<FromAddress>%%FromAddress%%</FromAddress>" +
          "<ReplyTo>%%ReplyTo%%</ReplyTo>" +
          "<Visibility>%%Visibility%%</Visibility>" +
          "<Subject>%%Subject%%</Subject>" +
          "<TrackingLevel>4</TrackingLevel>" +
          "<Encoding>6</Encoding>" +
          "<ListID>41565</ListID>" +
          "</Header>" +
          "<MessageBodies>" +
          "<HTMLBody><![CDATA[%%HTMLBody%%]]></HTMLBody>" +
          "</MessageBodies>" +
          "<ForwardToFriend>" +
          "<ForwardType>0</ForwardType>" +
          "</ForwardToFriend>" +
          "</SaveMailing>" +
          "</Body>" +
          "</Envelope>";
  private final int visibility;
  private String mailingName = "";
  private String mailingId = "";
  private String fromName = "";
  private String fromAddress = "";
  private String replyTo = "";
  private String subject = "";
  private String htmlBody = "";
  private String folderPath = "";

  public SaveMailingCommand(String mailingName, String mailingId, String fromName, String fromAddress, String replyTo, String subject, String htmlBody, String folderPath, int visibility) {
    this.mailingName = mailingName;
    this.fromName = fromName;
    this.fromAddress = fromAddress;
    this.replyTo = replyTo;
    this.subject = subject;
    this.htmlBody = htmlBody;
    this.folderPath = folderPath;
    this.mailingId = mailingId;
    this.visibility = visibility;
  }

  @Override
  public String toString() {
    String replacedString = saveOrUpdateMailTemplateRequest;
    replacedString = replacedString.replace("%%MailingName%%", mailingName);

    if (mailingId != null && !Objects.equals(mailingId, "")) {
      replacedString = replacedString.replace("%%MailingId%%", mailingId);
    }
    else {
      replacedString = replacedString.replace("<MailingID>%%MailingId%%</MailingID>", "");
    }

    replacedString = replacedString.replace("%%FromName%%", fromName);
    replacedString = replacedString.replace("%%FromAddress%%", fromAddress);
    replacedString = replacedString.replace("%%ReplyTo%%", replyTo);
    replacedString = replacedString.replace("%%Subject%%", subject);
    replacedString = replacedString.replace("%%HTMLBody%%", htmlBody);
    replacedString = replacedString.replace("%%FolderPath%%", folderPath);
    replacedString = replacedString.replace("%%Visibility%%", String.valueOf(visibility));

    return replacedString;
  }


  public IbmDigitalMarketingApiResponse postProcessResponse(Document xmlDocument, XPath xPath, Boolean isSuccess) throws XPathExpressionException {
    IbmDigitalMarketingApiResponse response;
    response = new SaveMailingCommandResponse(isSuccess);
    if (isSuccess) {
      String mailingId = xPath.compile(SaveMailingCommand.XPATH_MAILING_ID).evaluate(xmlDocument);
      ((SaveMailingCommandResponse) response).setMailingId(mailingId);
    }
    else {
      String faultString = xPath.compile(SaveMailingCommand.XPATH_FAULT_STRING).evaluate(xmlDocument);
      ((SaveMailingCommandResponse) response).setFaultString(faultString);
    }
    return response;
  }

}
