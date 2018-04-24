<#-- @ftlvariable name="self" type="com.coremedia.blueprint.common.contentbeans.CMTeasable" -->
<#-- @ftlvariable name="metadata" type="java.util.List" -->

<#if bp.setting(self,'disableCropping',false)>
  <img  alt="" <@preview.metadata (metadata![]) + [self.picture.content]/>
       src="${cm.getLink(self.picture.getTransformedData(imageFormat), {"width":imageWidth, "height":imageHeight,"absolute": true,"scheme": "http"})}"
        align="center" class="text-center"
    <@preview.metadata data=["properties.data"] />>


<#else>
  <img  alt=""
       src="${cm.getLink(self.picture
       .getTransformedData(imageFormat), {"width":imageWidth, "height":imageHeight,"absolute": true,"scheme": "http"})}"
        align="center" class="text-center"
    <@preview.metadata
    data=["properties.data"] />>

</#if>
