<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<@cm.responseHeader name="Content-Type" value="text/html; charset=UTF-8"/>
<#-- @ftlvariable name="self" type="com.coremedia.blueprint.common.contentbeans.Page" -->
<#-- @ftlvariable name="sliderMetadata" type="java.lang.String" -->
<html xmlns="http://www.w3.org/1999/xhtml" <@preview.metadata data=bp.getPageMetadata(self)!""/>>

<#assign studioExtraFilesMetadata=preview.getStudioAdditionalFilesMetadata(bp.setting(self, "studioPreviewCss"), bp.setting(self, "studioPreviewJs"))/>
<#assign mailingId=cmpage.context.segment/>
<#assign content=self.content/>
<#assign link = cm.getLink(content, {"absolute": true,"mailingId": mailingId})>
<#assign sliderMetadata={
"cm_preferredWidth": 1281,
"cm_responsiveDevices": {
<#-- list of the devices.
naming and icons see: BlueprintDeviceTypes.properties
the default icons are in studio-core, but you can define
your own style-classes in slider-icons.css.
-->
<#-- e.g. iphone6 -->
"mobile_portrait": {
"width": 375,
"height": 667,
"order": 1
},
"mobile_landscape": {
"width": 667,
"height": 375,
"order": 2
},
<#-- e.g. ipad -->
"tablet_portrait": {
"width": 768,
"height": 1024,
"order": 3
},
"tablet_landscape": {
"width": 1024,
"height": 768,
"order": 4
}}}/>
<head>
  <title<@preview.metadata "properties.title" />>${self.title!"CoreMedia CMS - No Page Title"}</title>

  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <meta name="viewport" content="width=device-width"/>

<#-- include PDE -->
<@preview.previewScripts/>


</head>

<body <@preview.metadata data=sliderMetadata />>
<#-- include css -->
  <#list self.css![] as css>
    <@cm.include self=css view="asCSSLink"/>
  </#list>
<!-- Wrapper for the body of the email -->
<table class="body">
    <tr>
        <!-- The class, align, and <center> tag center the container -->
        <td valign="top">

        <#-- HEADER -->
        <table class="container spacer--grey">
            <tbody>
            <tr>
                <td>
                    <table class="spacer"><tbody><tr><td height="10px" style="font-size:10px;line-height:10px;">&#xA0;</td></tr></tbody></table>

                    <table class="row">
                        <tbody>
                        <tr>
                            <#-- LOGO -->
                            <th class="small-6 large-6 columns first">
                                <p class="text-left">
                                    <a href="http://www.coremedia.com" class="newsletter_logo"></a>
                                </p>
                            </th>
                            <#-- /LOGO -->

                            <#-- NEWSLETTER LINK -->
                            <th class="small-6 large-6 columns last">
                                <p class="text-right" style="font-size: 12px;">
                                  ${bp.getMessage("newsletter_no_image")} <a href="${link}">${bp.getMessage("newsletter_click_here")}</a>
                                </p>
                            </th>
                            <#-- /NEWSLETTER LINK -->
                        </tr>
                        </tbody>
                    </table>


                </td>
            </tr>


            <#-- PICTURE -->
            <#assign pictures=content.pictures />
            <#if pictures?has_content>
            <tr>
                <td>
                    <#assign imageSrc=bp.getBiggestImageLink(pictures[0], "landscape_ratio16x9")/>
                    <img src="${imageSrc}" alt=""/>
                </td>
            </tr>
            </#if>
            <#-- /PICTURE -->

            </tbody>
        </table>
        <#-- /HEADER -->

      <table class="container">
        <tbody>
        <tr>
          <td>
            <table class="spacer spacer--grey">
              <tbody>
              <tr>
                <td height="10px" style="font-size:10px;line-height:10px;">&#xA0;</td>
              </tr>
              </tbody>
            </table>
            <table class="row">
              <tbody>
              <tr>
                <th class="small-12 large-12 columns last first">
                  <table>
                    <tbody>
                    <tr>
                      <th>
                        <h1 class="text-left" style="font-size: 36px;"<@preview.metadata "properties.teaserTitle" />>${content.teaserTitle}</h1>
                        <#if content.teaserText?has_content>
                          <@cm.include self=content.teaserText/>
                        </#if>
                      </th>
                    </tr>
                    </tbody>
                  </table>

                </th>
              </tr>
              </tbody>
            </table>
          </td>
        </tr>
        </tbody>
      </table>

      <table class="container">
        <tbody>
        <tr>
          <td>
            <table class="spacer spacer--grey">
              <tbody>
              <tr>
                <td height="20px" style="font-size:20px;line-height:20px;">&#xA0;</td>
              </tr>
              </tbody>
            </table>
          </td>
        </tr>
        </tbody>
      </table>
    <#-- MAIN MAIL BODY -->
    <#if self.pageGrid?has_content>
      <table class="container">
        <tbody>
        <tr>

          <td>
            <table class="spacer">
              <tbody>
              <tr>
                <td height="20px" style="font-size:20px;line-height:20px;">&#xA0;</td>
              </tr>
              </tbody>
            </table>

            <#list self.pageGrid.rows![] as row>
              <#list row.placements as placement>
                <#if ! (placement.name == 'footer' || placement.name == 'header' || placement.name == 'footer-advertisement' || placement.name == 'header-advertisement') >
                  <@cm.include self=placement view="asNewsletterItem"/>
                </#if>
              </#list>
            </#list>
          </td>
        </tr>
        </tbody>
      </table>
    </#if>

    <#-- FOOTER -->
    <table class="container" style="background-color: #f3f3f3;">
      <tbody>
      <tr>
        <td>
          <table class="spacer">
            <tbody>
            <tr>
              <td height="10px" style="font-size:10px;line-height:10px;">&#xA0;</td>
            </tr>
            </tbody>
          </table>

          <table class="row">
            <tbody>
            <tr>
              <th class="small-12 large-12 columns last">
                <p class="text-center">
                  <a href="http://www.coremedia.com" target="_blank" style="color: #666666; font-size: 12px;">&copy;&nbsp;${.now?string['yyyy']}&nbsp;${bp.getMessage("newsletter_company")}</a>&nbsp;
                  <a href="http://www.coremedia.com" target="_blank" style="color: #666666; font-size: 12px;">${bp.getMessage("newsletter_unsubscribe")}</a>&nbsp;
                  <a href="http://www.coremedia.com" target="_blank" style="color: #666666; font-size: 12px;">${bp.getMessage("newsletter_preferences")}</a>&nbsp;
                  <a href="http://www.coremedia.com" target="_blank" style="color: #666666; font-size: 12px;">${bp.getMessage("newsletter_privacy_policy")}</a>
                </p>
              </th>
            </tr>
            </tbody>
          </table>

        </td>
      </tr>
      </tbody>
    </table><#-- /FOOTER -->

    </td>
  </tr>
</table>
</body>
</html>

