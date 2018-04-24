<#-- @ftlvariable name="self" type="com.coremedia.livecontext.contentbeans.CMProductTeaser" -->
<#if self.product?has_content>

  <#if self.product.currency?has_content && self.product.locale?has_content>
    <#if self.product.listPrice?has_content>
      <#assign listPriceFormatted=lc.formatPrice(self.product.listPrice, self.product.currency, self.product.locale)/>
    </#if>
    <#if self.product.offerPrice?has_content>
      <#assign offerPriceFormatted=lc.formatPrice(self.product.offerPrice, self.product.currency, self.product.locale)/>
    </#if>
  </#if>
  <#assign showOfferPrice=(offerPriceFormatted!"") != "" && (offerPriceFormatted!"") != (listPriceFormatted!"") />

  <#assign classPrice="" />
<div class="price">
  <#if !bp.setting(cmpage, "pricesInImage", false)>
    <#if showOfferPrice>
        <span class="price--listprice">${listPriceFormatted!""}</span>
        <span class="price--offerprice">${offerPriceFormatted!""}</span>
    <#else>
        <span class="price--offerprice">${listPriceFormatted!""}</span>
    </#if>
  </#if>

</div>
</#if>