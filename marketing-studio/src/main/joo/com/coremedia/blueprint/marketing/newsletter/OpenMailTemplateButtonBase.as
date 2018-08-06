package com.coremedia.blueprint.marketing.newsletter {

import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;

import ext.Ext;
import ext.button.Button;
import ext.panel.Panel;
import ext.window.Window;

public class OpenMailTemplateButtonBase extends Button {

  const previewWindowId:String = "mailPreviewWindow";

  public function OpenMailTemplateButtonBase(config:OpenMailTemplateButton = null) {
    super(config);
  }

  public function openPreview():void {

    var mailingId:String = initialConfig.bindTo.extendBy("properties", "localSettings", "mailingId").getValue();
    var previewWindow:Window = Ext.getCmp(previewWindowId) as Window;
    if(previewWindow) {
      previewWindow.destroy();
    }
    var window:Window = new Window(Window({
      title: "Mailtemplate Preview",
      width: 800,
      id: previewWindowId,
      itemId: previewWindowId,
      height: 600,
      layout: 'fit',
      items: [new Panel(Panel({
        autoEl: {
          tag: "iframe",
          height: 600,
          width: 800,
          src: "/api/marketing/previewNewsletter/" + mailingId
        }
      }))]
    }));
    window.show();
    window.addListener("onblur", function () {
      window.destroy();
    });

  }

  internal function templateExistsExpression():ValueExpression {
    return ValueExpressionFactory.createFromFunction(function():Boolean {
      if(initialConfig.bindTo) {
        var mailingId:ValueExpression = initialConfig.bindTo.extendBy("properties", "localSettings");
        if(mailingId.extendBy("mailingId").getValue()) {
          return false;
        }
      }
      return true;
    });
  }

}
}
