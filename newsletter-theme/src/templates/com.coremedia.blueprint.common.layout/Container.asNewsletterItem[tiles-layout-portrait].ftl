<#-- @ftlvariable name="self" type="com.coremedia.blueprint.common.layout.Container" -->
<#assign properties={"imageFormat":"portrait_ratio3x4","imageWidth":"200", "imageHeight":"267", "imageMaxWidth":"209", "metadata": ["properties.items"]}/>
<#list self.items?chunk(3) as row>
<table class="row">
    <tbody>
    <tr>
      <#list row as cell>
        <#if cell_index == 0 && !cell_has_next>
          <#assign columnClass = "first last"/>
        <#elseif cell_index == 0 && cell_has_next>
          <#assign columnClass = "first"/>
        <#elseif !cell_has_next && (cell_index > 0) >
          <#assign columnClass = "last"/>
        <#else>
          <#assign columnClass = ""/>
        </#if>

          <th class="small-12 large-4 columns ${columnClass}">
            <@cm.include self=cell view="asNewsletterItem" params=properties/>
          </th>
      </#list>
    </tr>
    </tbody>
</table>
</#list>

