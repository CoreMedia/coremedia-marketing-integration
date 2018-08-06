package com.coremedia.blueprint.marketing.impl;

import com.coremedia.blueprint.marketing.MarketingContext;
import com.coremedia.cache.Cache;
import com.coremedia.cache.CacheKey;

/**
 * Cache key for caching the marketing context settings.
 */
public class MarketingContextProviderCacheKey extends CacheKey<MarketingContext> {
  private MarketingContextProvider marketingContextProvider;
  private String connectionId;

  MarketingContextProviderCacheKey(MarketingContextProvider marketingContextProvider, String connectionId) {
    this.marketingContextProvider = marketingContextProvider;
    this.connectionId = connectionId;
  }

  @Override
  public MarketingContext evaluate(Cache cache) {
    return marketingContextProvider.createContext(connectionId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MarketingContextProviderCacheKey)) {
      return false;
    }

    MarketingContextProviderCacheKey that = (MarketingContextProviderCacheKey) o;

    return this.connectionId.equals(that.connectionId);
  }

  @Override
  public int hashCode() {
    return connectionId.hashCode();
  }
}
