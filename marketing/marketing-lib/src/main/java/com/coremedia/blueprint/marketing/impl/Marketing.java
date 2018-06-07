package com.coremedia.blueprint.marketing.impl;
package com.coremedia.blueprint.marketing.impl;

import com.coremedia.blueprint.marketing.MarketingConnection;
import com.coremedia.blueprint.marketing.MarketingConnections;
import com.coremedia.blueprint.marketing.MarketingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This objects holds the currently available {@link MarketingConnections}.
 */
public class Marketing implements BeanFactoryAware, BeanNameAware, InitializingBean {
  private static final Logger LOG = LoggerFactory.getLogger(Marketing.class);
  private BeanFactory beanFactory;
  private String beanName;
  private Map<String, MarketingConnection> connections = new HashMap<>();

  @Nullable
  public MarketingConnection getConnection(MarketingContext marketingContext) {
    String connectionId = marketingContext.getConnectionId();
    if (!connections.containsKey(connectionId)) {
      MarketingConnection connection = createConnection(marketingContext);
      if (connection != null && marketingContext.isEnabled()) {
        connection.setMarketingContext(marketingContext);
        connections.put(marketingContext.getConnectionId(), connection);
      }
    }
    return connections.get(connectionId);
  }

  @Nonnull
  public MarketingConnections getConnections(List<MarketingContext> contexts) {
    Map<String, MarketingConnection> result = new HashMap<>();
    try {
      for (MarketingContext context : contexts) {
        MarketingConnection connection = getConnection(context);
        if(connection != null) {
          result.put(context.getConnectionId(), connection);
        }
        else {
          LOG.error("No connection configured for '" + context.getConnectionId() + "'");
        }
      }
    } catch (Exception e) {
      LOG.error("Could not retrieve marketing bean for connections {}.", e.getMessage(), e);
    }
    return new MarketingConnections(result);
  }


  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  public void afterPropertiesSet() {
    if (this.beanFactory == null) {
      throw new IllegalStateException("BeanFactory must be set.");
    }
    else if (this.beanName == null) {
      throw new IllegalStateException("BeanName must be set.");
    }
  }

  @Nullable
  private MarketingConnection createConnection(MarketingContext marketingContext) {
    String beanDefinitionId = this.beanName + ":" + marketingContext.getConnectionType();
    try {
      MarketingConnection prototypeBean = (MarketingConnection) beanFactory.getBean(beanDefinitionId);
      return prototypeBean;
    } catch (Exception e) {
      LOG.error("Could not retrieve marketing bean for connection bean id '{}'.", beanDefinitionId, e);
      return null;
    }
  }
}
