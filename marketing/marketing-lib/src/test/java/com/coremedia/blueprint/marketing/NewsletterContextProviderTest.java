package com.coremedia.blueprint.marketing;

import com.coremedia.blueprint.marketing.newsletter.NewsletterContext;
import com.coremedia.blueprint.marketing.impl.newsletter.NewsletterContextProvider;
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


/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {XmlRepoConfiguration.class, NewsletterContextProviderTest.LocalConfig.class})
public class NewsletterContextProviderTest {

  @Inject
  @Named("newsletterContextProvider")
  NewsletterContextProvider testling;

  @Test
  public void testMarketingContext() {
    NewsletterContext context = testling.createContext("ibm1");
    Assert.assertNull(context);

    context = testling.createContext("ibm");
    Assert.assertNotNull(context);

    context = testling.createContext("sfmc");
    Assert.assertNotNull(context);

    Assert.assertEquals("http://helios.livecontext.coremedia.com/blueprint/servlet", context.getLiveCaeUrl());
    Assert.assertEquals("CoreMedia Marketing Newsletter", context.getMailFromName());
    Assert.assertEquals("info@coremedia.com", context.getMailFromAddress());
    Assert.assertEquals("info@coremedia.com", context.getReplyAddress());
    Assert.assertEquals("deepLinkTemplate", context.getDeepLinkTemplate());
    Assert.assertEquals("Newsletter", context.getNewsletterTheme());
    Assert.assertEquals("sfmc1", context.getMarketingConnectionId());
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

