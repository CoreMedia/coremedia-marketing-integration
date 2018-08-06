package com.coremedia.blueprint.marketing.ibm.mailing;

import com.coremedia.blueprint.marketing.ibm.IbmDigitalMarketingApiCommand;
import com.coremedia.blueprint.marketing.ibm.IbmDigitalMarketingApiResponse;
import com.coremedia.blueprint.marketing.ibm.MarketingCloudClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class GetMailingTemplatesCommand extends IbmDigitalMarketingApiCommand {
  private static final Logger LOG = LoggerFactory.getLogger(GetMailingTemplatesCommand.class);

  private static final String XPATH_MAILING_TEMPLATES = "/Envelope/Body/RESULT/MAILING_TEMPLATE";

  private static String getMailingTemplatesRequest = "<Envelope>" +
          "<Body>" +
          "<GetMailingTemplates>" +
          "<VISIBILITY>%%visibility%%</VISIBILITY>" +
          "</GetMailingTemplates>" +
          "</Body>" +
          "</Envelope>";
  private final int visibility;


  public GetMailingTemplatesCommand(int visibility) {
    this.visibility = visibility;
  }

  @Override
  public String toString() {
    String replacedString = getMailingTemplatesRequest;
    replacedString = replacedString.replace("%%visibility%%", String.valueOf(visibility));
    return replacedString;
  }

  @Override
  public IbmDigitalMarketingApiResponse postProcessResponse(Document xmlDocument, XPath xPath, Boolean isSuccess) throws XPathExpressionException {
    GetMailingTemplatesCommandResponse response;
    response = new GetMailingTemplatesCommandResponse(isSuccess);

    if (isSuccess) {
      NodeList nodeList = (NodeList) xPath.compile(XPATH_MAILING_TEMPLATES).evaluate(xmlDocument, XPathConstants.NODESET);
      List<MailingTemplate> templates = new ArrayList<>();

      for (int i = 0; i < nodeList.getLength(); i++) {
        Element template = (Element) nodeList.item(i);
        String mailingId = xPath.evaluate("MAILING_ID", template);
        String mailingName = xPath.evaluate("MAILING_NAME", template);
        String lastModifiedDateStr = xPath.evaluate("LAST_MODIFIED", template);
        SimpleDateFormat formatter = new SimpleDateFormat(MarketingCloudClient.XML_API_DATE_FORMAT);
        Date lastModified = null;
        try {
          lastModified = formatter.parse(lastModifiedDateStr);
        } catch (ParseException e) {
          LOG.error("Could not parse date format from lastModifiedDate. dateStr=" + lastModifiedDateStr);
        }
        templates.add(new MailingTemplate(mailingId, mailingName, lastModified));
      }
      response.setMailingTemplates(templates);
    }
    else {
      LOG.error("Could not read mailing templates");
    }
    return response;
  }


}
