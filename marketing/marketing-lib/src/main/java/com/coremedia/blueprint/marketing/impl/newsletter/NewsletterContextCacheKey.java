package com.coremedia.blueprint.marketing.impl.newsletter;

import com.coremedia.blueprint.base.settings.SettingsService;
import com.coremedia.blueprint.marketing.newsletter.NewsletterContext;
import com.coremedia.cache.Cache;
import com.coremedia.cache.CacheKey;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.content.ContentRepository;
import com.coremedia.cap.multisite.Site;
import com.coremedia.cap.multisite.SitesService;
import com.coremedia.cap.struct.Struct;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Caching of the Newsletter settings.
 */
public class NewsletterContextCacheKey extends CacheKey<NewsletterContext> {
  private static final String NEWSLETTER_SETTINGS_DOCUMENT = "NewsletterSettings";
  private static final String NEWSLETTER_LIST = "newsletters";

  private static final String NEWSLETTER_ID = "newsletterId";

  private SettingsService settingsService;
  private ContentRepository contentRepository;
  private SitesService sitesService;
  private String globaConfigPath;
  private String siteConfigPath;
  private String connectionId;

  public NewsletterContextCacheKey(SettingsService settingsService,
                                   ContentRepository contentRepository,
                                   SitesService sitesService,
                                   String globaConfigPath,
                                   String siteConfigPath,
                                   String connectionId) {
    this.settingsService = settingsService;
    this.contentRepository = contentRepository;
    this.sitesService = sitesService;
    this.globaConfigPath = globaConfigPath;
    this.siteConfigPath = siteConfigPath;
    this.connectionId = connectionId;
  }

  @Override
  public NewsletterContext evaluate(Cache cache) {
    //Look in global config
    NewsletterContext context = findContextInSettings(connectionId, findNewsletterConnectionSettings(globaConfigPath
            + "/" + NEWSLETTER_SETTINGS_DOCUMENT));
    if (context != null) {
      return context;
    }
    //Look in all site configs
    for (Site site : sitesService.getSites()) {
      Content siteRootFolder = site.getSiteRootFolder();
      context = findContextInSettings(connectionId, findNewsletterConnectionSettings(siteRootFolder.getPath()
              + "/" + siteConfigPath + "/" + NEWSLETTER_SETTINGS_DOCUMENT));
      if (context != null) {
        return context;
      }
    }
    return null;
  }

  private List findNewsletterConnectionSettings(String location) {
    Content settings = contentRepository.getChild(location);
    if (settings != null) {
      return settingsService.settingWithDefault(NEWSLETTER_LIST, List.class, Collections.emptyList(), settings.get("settings"));
    }
    return Collections.emptyList();
  }

  @Nullable
  private NewsletterContext findContextInSettings(String id, List globalSettings) {
    for (Object setting : globalSettings) {
      if (setting instanceof Struct) {
        String connectionId = settingsService.setting(NEWSLETTER_ID, String.class, setting);
        if (StringUtils.equals(connectionId, id)) {
          return createContextFromStruct((Struct) setting);
        }
      }
    }
    return null;
  }

  private NewsletterContext createContextFromStruct(Struct settings) {
    return new NewsletterContextImpl(settings.getProperties());
  }

  @Override
  public int hashCode() {
    return connectionId.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NewsletterContextCacheKey)) {
      return false;
    }

    NewsletterContextCacheKey that = (NewsletterContextCacheKey) o;

    return this.connectionId.equals(that.connectionId);
  }
}
