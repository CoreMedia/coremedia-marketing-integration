package com.coremedia.blueprint.marketing.impl;

import com.coremedia.blueprint.base.settings.SettingsService;
import com.coremedia.blueprint.marketing.MarketingContext;
import com.coremedia.cache.Cache;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.cap.multisite.Site;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.cap.struct.Struct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarketingContextProvider {
  private static final String MARKETING_SETTINGS_DOCUMENT = "MarketingSettings";
  private final static String DEFAULT_GLOBAL_CONFIGURATION_PATH = "/Settings/Options/Settings";
  private final static String DEFAULT_SITE_CONFIGURATION_PATH = "/Options/Settings";
  private static final String MARKETING_CONNECTIONS = "marketingConnections";
  private static final String CONNECTION_ID = "connectionId";

  private SettingsService settingsService;
  private ContentRepository contentRepository;
  private SitesService sitesService;

  private String globalConfigPath, siteConfigPath;

  private Cache cache;

  public MarketingContext findContext(String connectionId) {
    return cache.get(new MarketingContextProviderCacheKey(this, connectionId));
  }

  /**
   * Used by cache key
   */
  protected MarketingContext createContext(String connectionId) {
    //Look in global config
    MarketingContext context = findContextInSettings(connectionId, findMarketingConnectionSettings(getGlobalConfigPath()
            + "/" + MARKETING_SETTINGS_DOCUMENT));
    if (context != null) {
      return context;
    }
    //Look in all site configs
    for (Site site : sitesService.getSites()) {
      Content siteRootFolder = site.getSiteRootFolder();
      context = findContextInSettings(connectionId, findMarketingConnectionSettings(siteRootFolder.getPath()
              + "/" + getSiteConfigPath() + "/" + MARKETING_SETTINGS_DOCUMENT));
      if (context != null) {
        return context;
      }
    }
    return null;
  }

  public List<MarketingContext> findContexts(@Nullable Site site) {
    String sideId = null;
    if(site != null) {
      sideId = site.getId();
    }
    return cache.get(new MarketingContextsProviderCacheKey(this, sitesService, sideId));
  }

  /**
   * Used by the marketing cache key
   * @param site the preferred site in the Studio
   * @return the list of available marketing contexts/connections.
   */
  protected List<MarketingContext> createContexts(Site site) {
    List<MarketingContext> result = new ArrayList<>();

    if(site != null) {
      Content siteRootFolder = site.getSiteRootFolder();
      List marketingConnectionSettings = findMarketingConnectionSettings(siteRootFolder.getPath()
              + getSiteConfigPath() + "/" + MARKETING_SETTINGS_DOCUMENT);
      for (Object setting : marketingConnectionSettings) {
        if (setting instanceof Struct) {
          result.add(new MarketingContextImpl(((Struct)setting).getProperties()));
        }
      }
    }

    List globalSettings = findMarketingConnectionSettings(getGlobalConfigPath() + "/" + MARKETING_SETTINGS_DOCUMENT);
    for (Object setting : globalSettings) {
      if (setting instanceof Struct) {
        result.add(new MarketingContextImpl(((Struct)setting).getProperties()));
      }
    }
    return result;
  }

  private List findMarketingConnectionSettings(String location) {
    Content settings = contentRepository.getChild(location);
    if (settings != null) {
      return settingsService.settingWithDefault(MARKETING_CONNECTIONS, List.class, Collections.emptyList(), settings.get("settings"));
    }
    return Collections.emptyList();
  }

  @Nullable
  private MarketingContext findContextInSettings(String id, List globalSettings) {
    for (Object setting : globalSettings) {
      if (setting instanceof Struct) {
        String connectionId = settingsService.setting(CONNECTION_ID, String.class, setting);
        if (StringUtils.equals(connectionId, id)) {
          return createContextFromStruct((Struct) setting);
        }
      }
    }
    return null;
  }

  private MarketingContext createContextFromStruct(Struct settings) {
    return new MarketingContextImpl(settings.getProperties());
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
