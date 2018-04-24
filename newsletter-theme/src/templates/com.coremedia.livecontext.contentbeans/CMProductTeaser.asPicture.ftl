<#-- @ftlvariable name="self" type="com.coremedia.livecontext.contentbeans.CMProductTeaser" -->

<#if !bp.setting(cmpage, "pricesInImage", false)>
 <#assign imageLinkUntouched=cm.getLink(self.picture.getTransformedData(imageFormat), {"width":imageWidth, "height":imageHeight,"absolute": true,"scheme": "http"})?replace("%3D", "=") />
<#else>
  <#assign imageLinkUntouched=cm.getLink(self.picture.getTransformedData(imageFormat), {"width":imageWidth, "height":imageHeight,"absolute": true,"scheme": "http", "productId" : self.product.id + "###" + self.getProductInSite().getSite().getId()})?replace("%3D", "=") />
</#if>

<div <@preview.metadata (metadata![]) + [self.picture.content]/>>
<#if bp.setting(self.picture,'disableCropping',false)>
  <img alt=""
       <#--src="${imageLinkSeperated[0] + "?" + queryParameters}"-->
       src="${imageLinkUntouched}"
       align="center" class="text-center"
        <@preview.metadata data=["properties.data"] />>

<#else>
  <img  alt=""
       src="${imageLinkUntouched}"
        align="center" class="text-center"
        <@preview.metadata data=["properties.data"] />>

</#if>
   </div>
