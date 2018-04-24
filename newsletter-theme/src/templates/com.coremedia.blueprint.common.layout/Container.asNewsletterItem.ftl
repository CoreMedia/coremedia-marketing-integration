<#-- @ftlvariable name="self" type="com.coremedia.blueprint.common.layout.Container" -->
<#assign properties={"imageFormat":"landscape_ratio16x9","imageWidth":"638", "imageHeight":"359", "imageMaxWidth":"638", "metadata": ["properties.items"]}/>
<#list self.items as item>
<table class="row">
    <tbody>
    <tr>
        <th class="small-12 large-12 columns first last">
          <@cm.include self=item view="asNewsletterItem" params=properties/>
        </th>
    </tr>
    </tbody>
</table>

</#list>
