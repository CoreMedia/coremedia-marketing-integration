package com.coremedia.blueprint.marketing.beans {
import com.coremedia.ui.data.impl.RemoteBeanImpl;

[RestResource(uriTemplate="marketing/marketings/{id:[^/]+}")]
public class MarketingsImpl extends RemoteBeanImpl implements Marketings {

  function MarketingsImpl(path:String) {
    super(path);
  }

  public function getMarketings():Array {
    return get(MarketingPropertyNames.MARKETINGS);
  }
}
}