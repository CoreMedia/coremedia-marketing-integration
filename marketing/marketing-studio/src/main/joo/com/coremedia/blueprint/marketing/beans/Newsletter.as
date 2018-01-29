package com.coremedia.blueprint.marketing.beans {

/**
 * The RemoteBean interface for a newsletter service.
 */
public interface Newsletter {

  function getDeepLinkTemplate():String;

  function getMailFromName():String;

  function getMailFromAddress():String;

  function getReplyToAddress():String;

  function getMarketingConnectionId():String;

  function getProperties():Object;

  function getMarketing():Marketing;

  function getPushTemplateUri():String;

  function getNewsletterTheme():String;
}
}