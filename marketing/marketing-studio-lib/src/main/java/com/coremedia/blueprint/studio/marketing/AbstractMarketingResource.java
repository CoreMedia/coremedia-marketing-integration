package com.coremedia.blueprint.studio.marketing;

import com.coremedia.blueprint.marketing.impl.Marketing;
import com.coremedia.rest.linking.AbstractLinkingResource;
import org.springframework.beans.factory.annotation.Required;

public class AbstractMarketingResource extends AbstractLinkingResource {
  private Marketing marketing;

  public Marketing getMarketing() {
    return marketing;
  }

  @Required
  public void setMarketing(Marketing marketing) {
    this.marketing = marketing;
  }
}
