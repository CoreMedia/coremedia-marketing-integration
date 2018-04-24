<#-- @ftlvariable name="self" type="com.coremedia.blueprint.common.contentbeans.CMTeaser" -->
<#-- @ftlvariable name="metadata" type="java.util.List" -->
<#assign mailingId=cmpage.context.segment/>
<#assign link = cm.getLink(self, {"absolute": true,"mailingId": mailingId})>
<table <@preview.metadata (metadata![]) + [self.content] />>
    <tr>
        <th>
        <#if self.picture?has_content>
            <a href="${link}">
              <@cm.include self=self view="asPicture" />
            </a>
        </#if>
        <#if self.teaserTitle?has_content>
            <h1>
              <#if self.target?has_content>
                  <a href="${link}"
                     target="_blank" <@preview.metadata "properties.teaserTitle" />>${self.teaserTitle}</a>
              <#else>
              ${self.teaserTitle}
              </#if>
            </h1>
        </#if>
        <#if self.teaserText?has_content>
            <a href="${link}">
                <p <@preview.metadata "properties.teaserText" />>
                  <#assign text = bp.truncateText(self.teaserText!"", 600) />
              ${text!""}
                </p>
            </a>
        </#if>
        </th>
    </tr>
</table>
