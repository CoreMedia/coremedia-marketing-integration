package com.coremedia.blueprint.marketing.impl;

import com.coremedia.blueprint.marketing.MarketingContext;
import com.coremedia.cache.Cache;
import com.coremedia.cache.CacheKey;
import com.coremedia.cap.multisite.Site;
import com.coremedia.cap.multisite.SitesService;

import java.util.List;

/**
 * Cache key to store the list of available marketing contexts.
 */
public class MarketingContextsProviderCacheKey extends CacheKey<List<MarketingContext>> {
  private MarketingContextProvider marketingContextProvider;
  private SitesService sitesService;
  private String siteId;

  MarketingContextsProviderCacheKey(MarketingContextProvider marketingContextProvider, SitesService sitesService, String siteId) {
    this.marketingContextProvider = marketingContextProvider;
    this.sitesService = sitesService;
    this.siteId = siteId;
  }

  @Override
  public List<MarketingContext> evaluate(Cache cache) {
    Site site = sitesService.getSite(siteId);
    return marketingContextProvider.createContexts(site);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MarketingContextsProviderCacheKey)) {
      return false;
    }

    MarketingContextsProviderCacheKey that = (MarketingContextsProviderCacheKey) o;

    if(this.siteId != null && that.siteId != null) {
      return this.siteId.equals(that.siteId);
    }

    return this.equals(that);
  }

  @Override
  public int hashCode() {
    return marketingContextProvider.hashCode();
  }
}
