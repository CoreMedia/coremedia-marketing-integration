package com.coremedia.blueprint.marketing {

import com.coremedia.blueprint.marketing.beans.MarketingImpl;
import com.coremedia.blueprint.marketing.beans.NewsletterImpl;
import com.coremedia.blueprint.marketing.beans.NewsletterPropertyNames;
import com.coremedia.cap.content.Content;
import com.coremedia.cms.editor.sdk.premular.PropertyFieldGroup;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;

import ext.DateUtil;
import ext.StringUtil;

import mx.resources.ResourceManager;

[ResourceBundle('com.coremedia.blueprint.marketing.MarketingStudioPlugin')]
public class NewsletterFormBase extends PropertyFieldGroup {

  [Bindable]
  public var newsletterId:String;

  private var openInMarketingSystemExpression:ValueExpression;
  private var newsletterValueExpression:ValueExpression;
  private var activeItemValueExpression:ValueExpression;

  public function NewsletterFormBase(config:NewsletterForm = null) {
    super(config);
  }

  internal function getActiveItemExpression(config:NewsletterForm):ValueExpression {
    if (!activeItemValueExpression) {
      activeItemValueExpression = ValueExpressionFactory.createFromFunction(function ():String {
        var ve:ValueExpression = config.bindTo;
        var content:Content = ve.getValue();

        var themes:Array = content.getProperties().get("theme");
        if (themes.length > 0) {
          var themeContent:Content = themes[0];
          if (!themeContent.isLoaded()) {
            themeContent.load();
            return undefined;
          }
          var newsletter:NewsletterImpl = getNewsletterValueExpression(this.initialConfig).getValue();
          if (!newsletter.isLoaded()) {
            newsletter.load();
            return undefined;
          }

          var themeName:String = newsletter.getNewsletterTheme();
          var name:String = themes[0].getName().toLowerCase();
          var nameSegments:Array = name.split(" ");
          for each(var seg:String in nameSegments) {
            if (themeName.toLowerCase() === seg.toLowerCase()) {
              return NewsletterForm.FORM_ITEM_ID;
            }
          }
        }
        return NewsletterForm.INVALID_FORM_ITEM_ID;
      });
    }
    return activeItemValueExpression;

  }

  internal function openInMarketingSystem():void {
    var link:String = getOpenInMarketingSystemExpression().getValue();
    window.open(link, "_blank");
  }

  internal function transformDate(millis:String):String {
    var date:Date = new Date(parseInt(millis));
    return DateUtil.format(date, ResourceManager.getInstance().getString('com.coremedia.cms.editor.Editor', 'dateFormat'));
  }

  internal function transformSpacerVisibility(value:String):Boolean {
    return value === null || value === undefined || value.length === 0;
  }

  protected function getNewsletterValueExpression(config:*):ValueExpression {
    if (!newsletterValueExpression) {
      newsletterValueExpression = ValueExpressionFactory.createFromValue(NewsletterImpl.create(config.newsletterId));
    }
    return newsletterValueExpression;
  }

  protected function getOpenInMarketingSystemExpression(config:NewsletterForm = null):ValueExpression {
    if (!openInMarketingSystemExpression) {
      openInMarketingSystemExpression = ValueExpressionFactory.createFromFunction(function ():Boolean {
        var newsletter:NewsletterImpl = getNewsletterValueExpression(config).getValue();
        if (!newsletter.isLoaded()) {
          newsletter.load();
          return undefined;
        }
        var template:String = newsletter.getDeepLinkTemplate();
        var templateIdProperty:String = newsletterId + "-" + NewsletterPropertyNames.TEMPLATE_ID;
        var templateId:String = config.bindTo.extendBy('properties.localSettings').getValue().get(templateIdProperty);

        if (templateId) {
          return StringUtil.format(template, templateId);
        }
        return null;
      });
    }
    return openInMarketingSystemExpression;
  }

  protected function getTextExpression(config:NewsletterForm, msg:String):ValueExpression {
    return ValueExpressionFactory.createFromFunction(function ():String {
      var newsletterImpl:NewsletterImpl = NewsletterImpl.create(config.newsletterId);
      if (!newsletterImpl.isLoaded()) {
        newsletterImpl.load();
        return undefined;
      }

      var marketing:MarketingImpl = newsletterImpl.getMarketing() as MarketingImpl;
      if (!marketing.isLoaded()) {
        marketing.load();
        return undefined;
      }

      var vendor:String = marketing.getVendorName();
      return StringUtil.format(msg, vendor, newsletterImpl.getNewsletterTheme());
    });
  }
}
}
