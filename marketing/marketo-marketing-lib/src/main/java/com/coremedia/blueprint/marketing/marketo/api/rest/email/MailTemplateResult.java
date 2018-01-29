package com.coremedia.blueprint.marketing.marketo.api.rest.email;

import com.coremedia.blueprint.marketing.marketo.api.rest.MarketoResponse;

/**
 * {
 "success": true,
 "warnings": [ ],
 "errors": [ ],
 "requestId": "a99f#14e22b2b85e",
 "result": [
 {
 "id": 1022,
 "name": "Sample Email Template",
 "description": "Create email template using API",
 "createdAt": "2015-06-23T23:13:34Z+0000",
 "updatedAt": "2015-06-23T23:13:34Z+0000",
 "url": "https://app-abm.marketo.com/#ET1022B2ZN12",
 "folder": {
 "type": "Folder",
 "value": 15,
 "folderName": "Templates"
 },
 "status": "draft",
 "workspace": "Default",
 "version": 1
 }
 ]
 }
 * http://developers.marketo.com/rest-api/assets/email-templates/#create_and_update
 */
public class MailTemplateResult extends MarketoResponse {
  private int id;
  private String name;
  private String description;
  private String content;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
