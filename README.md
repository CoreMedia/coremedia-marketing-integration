![Status: Active](https://documentation.coremedia.com/badges/badge_status_active.png "Status: Active")
![For CoreMedia CMS](https://documentation.coremedia.com/badges/badge_coremedia_cms.png "For CoreMedia CMS")
![Tested: 3.1801.2](https://documentation.coremedia.com/badges/badge_tested_coremedia_9-1801-2.png "Tested: 3.1801.2")

![CoreMedia Labs Logo](https://documentation.coremedia.com/badges/banner_coremedia_labs_wide.png "CoreMedia Labs Logo Title Text")


# Coremedia Marketing Integration

This CoreMedia extension will allow you to integrate just about any marketing tool or platform into your CoreMedia system. 
**We have provided out-of-the-box integration implementations for IBM Watson, Salesforce Marketing Cloud and Marketo.**

Employ the beauty and power of the Studio interface to craft your newsletter using templates. Drag and drop content into your newsletter and perfect the look of it with our immediate preview update.  Then send it with the push of a button to your marketing tool. Reaching your customers via email was never easier. 

This extension provides a generic interface which can be used to implement integrations for practically any marketing tool with an API that enables newsletter customization. We've also included the integration implementation for marketing systems such as Salesforce Marketing Cloud, Marketo or IBM Watson Campaign Automation which can be used out-of-the box. 

The configuration is done through settings and templates which can be conveniently created in CoreMedia Studio. Extending or  even looking at the code is completely unneccesary, as all setup for connections to the marketing system, options and templates is kept cleanly separated from the extension code. 

Here is an overview of the steps to take to install and configure the extension. The details for these steps together with example settings can be found in the [wiki](../../wiki). 

**1.  Get the extension**

To install the extension, download the project as zip file and unpack it into the extensions folder of your workspace. The  add the marketing extension to your extensions.properties file. Then run the extension tool on your workspace to enable the extension and build with maven. 

**2. Configure the connection to the marketing system**

Every marketing system integration consists of a connection and services that are attached to the connection. The first available service is the NewsletterService which is implemented for IBM, Salesforce and Marketo. The connection is configured using a Settings document containing the name and location of the service and the credentials. 

**3. Add the setup configuration for the Newsletter Service**

Here you will configure the settings for the service, such as the replyTo and from email address, and the template to use for rendering. This is also done via a Settings document.

**4. Add the Marketing UI to Studio**

Configure the Studio form for pages to show a Marketing tab. This tab will allow you to push your newsletter to your configured marketing tool directly from Studio. 

**5. Create a template for your newsletter**

The code repository for the Marketing integrations also contains an example theme which you may customize for your needs. The example templates have specific placeholders for IBM which may need to be changed. Alternatively, you may create your own theme using our frontend developer workflow. 

That's it! After these 5 easy steps, your extension is installed and ready to use. 

*******


# CoreMedia Labs

Welcome to [CoreMedia Labs](https://blog.coremedia.com/labs/)! This repository is part of a platform for developers who want
to have a look under the hood or get some hands-on understanding of the vast and compelling capabilities of CoreMedia.
Whatever your experience level with CoreMedia is, we've got something for you.

Each project in our Labs platform is an extra feature to be used with CoreMedia, including extensions,
tools and 3rd party integrations. We provide some test data and explanatory videos for non-customers and for insiders there is open-source code and instructions on integrating the feature into your CoreMedia workspace.

The code we provide is meant to be example code, illustrating a set of features that could be used to enhance your CoreMedia experience.
We'd love to hear your feedback on use-cases and further developments!
If you're having problems with our code, please refer to our issues section.
