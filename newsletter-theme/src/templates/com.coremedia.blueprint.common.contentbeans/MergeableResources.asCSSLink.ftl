<#-- @ftlvariable name="self" type="com.coremedia.blueprint.common.contentbeans.MergeableResources" -->

<#-- This template is used for merged (and minified) css files -->

<style type="text/css">
<#list self.mergeableResources as css>
  <@cm.include self=css.code view="plain"/>
</#list>
</style>