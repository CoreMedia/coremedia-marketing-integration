package com.coremedia.blueprint.marketing.ibm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

public abstract class IbmDigitalMarketingApiCommand {
  private static final Logger LOG = LoggerFactory.getLogger(IbmDigitalMarketingApiCommand.class);
  private static final String XPATH_IS_SUCCESS = "/Envelope/Body/RESULT/SUCCESS";
  private static final String XPATH_IS_FAULT = "/Envelope/Body/Fault/FaultString";

  public abstract String toString();

  public abstract IbmDigitalMarketingApiResponse postProcessResponse(Document xmlDocument, XPath xPath, Boolean isSuccess) throws XPathExpressionException;

  public IbmDigitalMarketingApiResponse postProcessResponseInternal(InputStream inputStream) {
    IbmDigitalMarketingApiResponse response = null;
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    try {
      builder = builderFactory.newDocumentBuilder();

      Document xmlDocument = builder.parse(inputStream);

      XPath xPath = XPathFactory.newInstance().newXPath();
      String isSuccessStr = xPath.compile(XPATH_IS_SUCCESS).evaluate(xmlDocument);
      Boolean isSuccess = Boolean.valueOf(isSuccessStr);

      if (!isSuccess) {
        String faultString = xPath.compile(XPATH_IS_FAULT).evaluate(xmlDocument);
        LOG.error(faultString);
      }

      response = postProcessResponse(xmlDocument, xPath, isSuccess);
    } catch (Exception e) {
      LOG.error("failed too parse API response", e);
    }
    return response;
  }
}
