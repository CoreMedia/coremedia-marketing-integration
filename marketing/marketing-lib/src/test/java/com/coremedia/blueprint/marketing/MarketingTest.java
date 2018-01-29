package com.coremedia.blueprint.marketing;

import com.coremedia.blueprint.marketing.impl.Marketing;
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


/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {XmlRepoConfiguration.class, MarketingTest.LocalConfig.class})
public class MarketingTest {

  @Inject
  @Named("marketing")
  private Marketing testling;

  @Inject
  @Named("marketingContextProvider")
  private MarketingContextProvider marketingContextProvider;

  @Test
  public void testMarketingContext() {
    List<MarketingContext> contexts = marketingContextProvider.findContexts(null);

    MarketingConnections connections = testling.getConnections(contexts);
    Assert.assertFalse(connections.getMarketingConnections().isEmpty());
    Assert.assertEquals(connections.getMarketingConnections().size(), contexts.size());
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

