package com.coremedia.blueprint.marketing.marketo.api.rest.folder;

/**
 *
 * "name": "Test 10 - deverly",
 "description": "This is a test",
 "createdAt": "2015-06-23T06:27:04Z+0000",
 "updatedAt": "2015-06-23T06:27:04Z+0000",
 "url": "https://app-abm.marketo.com/#MF1070A1",
 "folderId": {
 "id": 454,
 "type": "FOLDER"
 },
 "folderType": "Marketing Folder",
 "parent": {
 "id": 416,
 "type": "FOLDER"
 },
 "path": "/Marketing Activities/Default/Marketing Programs - deverly/Test 10 - deverly",
 "isArchive": false,
 "isSystem": false,
 "accessZoneId": 1,
 "workspace": "Default",
 "id": 454
 */
public class FolderResult {
  private String folderType;
  private int id;
  private FolderId folderId;
  private String path;
  private String name;
  private String description;

  public String getFolderType() {
    return folderType;
  }

  public void setFolderType(String folderType) {
    this.folderType = folderType;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
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

  public FolderId getFolderId() {
    return folderId;
  }

  public void setFolderId(FolderId folderId) {
    this.folderId = folderId;
  }
}
