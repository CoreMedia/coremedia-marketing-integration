<#-- @ftlvariable name="self" type="com.coremedia.blueprint.common.contentbeans.CodeResources" -->
<#list self.mergeableResources as item>
  <@cm.include self=item.code view="script" params={"absolute": true}/>
</#list>