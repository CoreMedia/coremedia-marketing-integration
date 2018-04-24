<#-- @ftlvariable name="self" type="com.coremedia.blueprint.common.contentbeans.CMPlaceholder" -->
<#if self.title?has_content>
  <h2>${self.title}</h2>
</#if>
<#if self.id?has_content>
  ${self.id}
</#if>
