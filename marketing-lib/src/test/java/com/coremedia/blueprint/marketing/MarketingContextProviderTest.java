package com.coremedia.blueprint.marketing;

import com.coremedia.blueprint.marketing.impl.MarketingContextProvider;
import com.coremedia.cap.test.xmlrepo.XmlRepoConfiguration;
import com.coremedia.cap.test.xmlrepo.XmlUapiConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {XmlRepoConfiguration.class, MarketingContextProviderTest.LocalConfig.class})
public class MarketingContextProviderTest {

  @Inject
  @Named("marketingContextProvider")
  private MarketingContextProvider testling;

  @Test
  public void testMarketingContext() {
    List<MarketingContext> contexts = testling.findContexts(null);
    Assert.assertFalse(contexts.isEmpty());

    MarketingContext ibm = testling.findContext("ibm1");
    Assert.assertNotNull(ibm);
    Assert.assertEquals("ibm", ibm.getConnectionType());
    Assert.assertEquals("IBM Silverpop", ibm.getVendorName());
    Assert.assertEquals("1", ibm.getVendorVersion());
    Assert.assertEquals("url", ibm.getVendorUrl());
    Assert.assertTrue(ibm.isEnabled());
    Assert.assertEquals("clientId", ibm.getProperty("clientId"));
    Assert.assertEquals("ibm1", ibm.getConnectionId());
  }

  @Configuration
  @ImportResource(value = {
          "classpath:META-INF/coremedia/component-marketing-lib.xml"
  },
          reader = com.coremedia.springframework.xml.ResourceAwareXmlBeanDefinitionReader.class)
  public static class LocalConfig {

    @Bean
    public XmlUapiConfig xmlUapiConfig() {
      return new XmlUapiConfig("classpath:/com/coremedia/blueprint/marketing/marketing-lib-test-content.xml");
    }
  }
}

