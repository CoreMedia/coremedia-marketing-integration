package com.coremedia.blueprint.marketing {
import com.coremedia.cms.editor.configuration.StudioPlugin;
import com.coremedia.cms.editor.sdk.IEditorContext;

public class MarketingStudioPluginBase extends StudioPlugin {
  public function MarketingStudioPluginBase(config:MarketingStudioPlugin = null) {
    super(config);
  }

  override public function init(editorContext:IEditorContext):void {
    super.init(editorContext);
  }
}
}
