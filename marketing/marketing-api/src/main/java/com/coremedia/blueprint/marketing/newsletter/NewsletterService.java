package com.coremedia.blueprint.marketing.newsletter;

import com.coremedia.blueprint.marketing.MarketingActionResponse;
import com.coremedia.blueprint.marketing.MarketingContext;

/**
 * Common interface to be implemented by marketing system that support newsletter
 */
public interface NewsletterService {

  /**
   * Sends a newsletter for the given content
   *
   * @param templateId an optional template id if an existing template should be updated
   * @param title      the title used for the template
   * @param body       the HTML body used as template
   * @return the marketing service call result
   */
  MarketingActionResponse pushTemplate(MarketingContext marketingContext, NewsletterContext context, String templateId, String title, String body);
}
