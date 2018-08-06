package com.coremedia.blueprint.marketing.beans {
import com.coremedia.ui.data.impl.RemoteBeanImpl;

[RestResource(uriTemplate="marketing/marketing/{id:[^/]+}")]
public class MarketingImpl extends RemoteBeanImpl implements Marketing {

  function MarketingImpl(path:String) {
    super(path);
  }


  public function getVendorName() {
    return get(MarketingPropertyNames.VENDOR_NAME);
  }

  public function getVendorUrl() {
    return get(MarketingPropertyNames.VENDOR_URL);
  }

  public function getVendorVersion() {
    return get(MarketingPropertyNames.VENDOR_VERSION);
  }
}
}