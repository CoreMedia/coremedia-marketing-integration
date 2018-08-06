package com.coremedia.blueprint.marketing.ibm.mailing;


import java.util.Date;

public class MailingTemplate {
  private String id;
  private String name;
  private Date lastModified;

  MailingTemplate(String id, String name, Date lastModified) {
    this.id = id;
    this.name = name;
    this.lastModified = lastModified;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Date getLastModified() {
    return lastModified;
  }
}
