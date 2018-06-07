package com.coremedia.blueprint.marketing.impl.newsletter;

import com.coremedia.blueprint.base.settings.SettingsService;
import com.coremedia.blueprint.marketing.newsletter.NewsletterContext;
import com.coremedia.cache.Cache;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.cap.multisite.SitesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class NewsletterContextProvider {
  private final static String DEFAULT_GLOBAL_CONFIGURATION_PATH = "/Settings/Options/Settings";
  private final static String DEFAULT_SITE_CONFIGURATION_PATH = "/Options/Settings";

  private SettingsService settingsService;
  private ContentRepository contentRepository;
  private SitesService sitesService;
  private Cache cache;

  private String globalConfigPath, siteConfigPath;

  public NewsletterContext createContext(String connectionId) {
    NewsletterContextCacheKey cacheKey = new NewsletterContextCacheKey(settingsService,
            contentRepository, sitesService, getGlobalConfigPath(), getSiteConfigPath(), connectionId);
    return cache.get(cacheKey);
  }

  @Required
  public void setContentRepository(ContentRepository contentRepository) {
    this.contentRepository = contentRepository;
  }

  @Required
  public void setSettingsService(SettingsService settingsService) {
    this.settingsService = settingsService;
  }

  @Required
  public void setSitesService(SitesService sitesService) {
    this.sitesService = sitesService;
  }

  public String getGlobalConfigPath() {
    return StringUtils.isEmpty(globalConfigPath) ? DEFAULT_GLOBAL_CONFIGURATION_PATH : globalConfigPath;
  }

  public void setGlobalConfigPath(String globalConfigPath) {
    this.globalConfigPath = globalConfigPath;
  }

  public String getSiteConfigPath() {
    return StringUtils.isEmpty(siteConfigPath) ? DEFAULT_SITE_CONFIGURATION_PATH : siteConfigPath;
  }

  public void setSiteConfigPath(String siteConfigPath) {
    this.siteConfigPath = siteConfigPath;
  }

  @Required
  public void setCache(Cache cache) {
    this.cache = cache;
  }
}
