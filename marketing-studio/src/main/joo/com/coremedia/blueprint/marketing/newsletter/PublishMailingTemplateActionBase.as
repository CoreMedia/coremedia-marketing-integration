package com.coremedia.blueprint.marketing.newsletter {
import com.coremedia.blueprint.marketing.*;

import com.coremedia.blueprint.marketing.beans.Newsletter;
import com.coremedia.blueprint.marketing.beans.NewsletterPropertyNames;
import com.coremedia.cap.common.IdHelper;
import com.coremedia.cap.content.Content;
import com.coremedia.cap.struct.Struct;
import com.coremedia.cms.editor.sdk.actions.ContentAction;
import com.coremedia.cms.editor.sdk.util.MessageBoxUtil;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.impl.RemoteService;
import com.coremedia.ui.util.RequestCounter;

import ext.Component;
import ext.LoadMask;

import js.XMLHttpRequest;

import mx.resources.ResourceManager;

[ResourceBundle('com.coremedia.blueprint.marketing.MarketingStudioPlugin')]
public class PublishMailingTemplateActionBase extends ContentAction {

  private var loadMaskCmp:LoadMask;
  private var bindToExpression:ValueExpression;
  private var newsletter:Newsletter;
  private var newsletterId:String;

  private var button:Component;
  private var parentComponent:Component;

  public function PublishMailingTemplateActionBase(config:PublishMailingTemplateAction = null) {
    newsletter = config.newsletterValueExpression.getValue();
    bindToExpression = config.contentValueExpression;
    newsletterId = config.newsletterId;
    config.handler = handlePublish;

    super(config);
    if (!bindToExpression) {
      bindToExpression = super.getValueExpression();
    }
  }


  override public function addComponent(comp:Component):void {
    super.addComponent(comp);
    this.parentComponent = comp.findParentByType(NewsletterForm.xtype);
    this.button = comp;
  }

  private function handlePublish():void {
    var contentBean:Content = bindToExpression.getValue();
    var contentId:Number = IdHelper.parseContentId(contentBean);

    setBusy(true);


    var templateIdExpression:ValueExpression = bindToExpression.extendBy('properties', 'localSettings', newsletterId + '-' + NewsletterPropertyNames.TEMPLATE_ID);
    var templateId:String = templateIdExpression.getValue();

    var params:Object = {
      contentId: contentId,
      templateId: templateId
    };

    var requestObject:Object = {
      url: newsletter.getPushTemplateUri(),
      method: 'POST',
      params: params,
      success: onReadSuccess,
      failure: onReadFailure
    };
    RequestCounter.openRequest();
    try {
      RemoteService.getConnection().request(requestObject);
    } catch (e:*) {
      RequestCounter.closeRequest();
      throw new Error("Unable to send Bean " + requestObject.method + " request: " + e);
    }
  }

  public function onReadSuccess(response:XMLHttpRequest):void {
    setBusy(false);
    var newValues:Object = RemoteService.resolveResponse(response.responseText, "");
    var success:* = newValues["success"];
    if (success === true) {
      var mailingId:String = newValues["id"];
      var localSettingsExpression:ValueExpression = bindToExpression.extendBy("properties", "localSettings");
      var localSettings:Struct = localSettingsExpression.getValue();
      var checkedIn:Boolean = (bindToExpression.getValue() as Content).isCheckedIn();

      var templateIdProperty:String = newsletterId + "-" + NewsletterPropertyNames.TEMPLATE_ID;
      localSettings.set(templateIdProperty, mailingId);

      var date:Date = newValues["lastModified"];
      if (date) {
        var lastModifiedProperty:String = newsletterId + "-" + NewsletterPropertyNames.LAST_MODIFIED;
        localSettings.set(lastModifiedProperty, "" + date.valueOf());
      }

      if (checkedIn) {
        (bindToExpression.getValue() as Content).checkIn();
      }

      MessageBoxUtil.showInfo(ResourceManager.getInstance().getString('com.coremedia.blueprint.marketing.MarketingStudioPlugin', 'template_upload_successful_title'),
              ResourceManager.getInstance().getString('com.coremedia.blueprint.marketing.MarketingStudioPlugin', 'template_upload_successful_text'));
    } else {
      MessageBoxUtil.showError(ResourceManager.getInstance().getString('com.coremedia.blueprint.marketing.MarketingStudioPlugin', 'template_upload_failed_title'), newValues.result);
      setBusy(false);
    }
  }

  public function onReadFailure(response:XMLHttpRequest):void {
    setBusy(false);
    RequestCounter.closeRequest();
    MessageBoxUtil.showError("Not successful", "Timeout or connection error");
  }

  private function setBusy(b:Boolean):void {
    if (b) {
      if (!loadMaskCmp) {
        var loadmaskCfg:LoadMask = LoadMask({
          target: parentComponent
        });
        loadmaskCfg.msg = ResourceManager.getInstance().getString('com.coremedia.blueprint.marketing.MarketingStudioPlugin', 'uploading_template');
        loadmaskCfg.target = parentComponent;
        var myMask:LoadMask = new LoadMask(loadmaskCfg);
        loadMaskCmp = myMask;
      }
      loadMaskCmp.show();
    }
    else {
      loadMaskCmp.hide();
    }
    this.button.setDisabled(b);
  }


  override protected function isDisabledFor(contents:Array):Boolean {
    var notAvailable:Boolean = contents.some(function (content:Content):Boolean {
      return !content.isCheckedIn();
    });
    return notAvailable;
  }


}
}
