package com.coremedia.blueprint.marketing.beans {
import com.coremedia.ui.data.beanFactory;
import com.coremedia.ui.data.impl.RemoteBeanImpl;

[RestResource(uriTemplate="marketing/newsletter/{id:[^/]+}")]
public class NewsletterImpl extends RemoteBeanImpl implements Newsletter {

  function NewsletterImpl(path:String) {
    super(path);
  }

  public static function create(newsletterId:String):NewsletterImpl {
    return beanFactory.getRemoteBean('marketing/newsletter/' + newsletterId) as NewsletterImpl;
  }

  public function getDeepLinkTemplate():String {
    return get(NewsletterPropertyNames.DEEP_LINK_TEMPLATE);
  }


  public function getMailFromName():String {
    return get(NewsletterPropertyNames.MAIL_FROM_NAME);
  }

  public function getMailFromAddress():String {
    return get(NewsletterPropertyNames.MAIL_FROM_ADDRESS);
  }

  public function getReplyToAddress():String {
    return get(NewsletterPropertyNames.MAIL_REPLY_TO_ADDRESS);
  }

  public function getProperties():Object {
    return get(NewsletterPropertyNames.PROPERTIES);
  }

  public function getMarketingConnectionId():String {
    return get(NewsletterPropertyNames.MARKETING_CONNECTION_ID);
  }

  public function getMarketing():Marketing {
    return get(MarketingPropertyNames.MARKETING);
  }

  public function getPushTemplateUri():String {
    return get(NewsletterPropertyNames.PUSH_TEMPLATE_URI);
  }

  public function getNewsletterTheme():String {
    return get(NewsletterPropertyNames.NEWSLETTER_THEME);
  }
}
}