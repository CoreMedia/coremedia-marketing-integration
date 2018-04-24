<#-- @ftlvariable name="self" type="com.coremedia.livecontext.contentbeans.CMProductTeaser" -->
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
        <#elseif self.product.catalogPicture?has_content>
            <a href="${link}">
                <@cm.include self=self.product.catalogPicture/>
            </a>
        </#if>
        <#if self.teaserTitle?has_content>
            <h1>
                <a href="${link}"
                   target="_blank" <@preview.metadata "properties.teaserTitle" />>${self.teaserTitle}</a>
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
            <table class="row">
                <tbody>
                <tr>

                    <th class="small-12 large-12 columns">
                        <table>
                            <tr>
                                <th>
                                <@cm.include self=self view="price"/>
                                </th>
                            </tr>
                        </table>
                    </th>
                </tr>
                </tbody>
            </table>
        </th>
    </tr>
</table>

