package com.coremedia.blueprint.studio.marketing.representation;

public class AbstractMarketingResourceRepresentation {

  private String type, id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
