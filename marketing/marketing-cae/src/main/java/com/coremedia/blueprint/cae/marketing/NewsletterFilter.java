package com.coremedia.blueprint.cae.marketing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * This filter is used to ensure that only absolute links are rendered by the CAE.
 * We do this by checking the "absolute" parameter and setting it as request attribute
 * so that the link builder will read it.
 */
public class NewsletterFilter implements Filter {
  private static final Logger LOG = LoggerFactory.getLogger(NewsletterFilter.class);
  private static final String ABSOLUTE = "absolute";

  @Override
  public void init(FilterConfig filterConfig) {
    // Do nothing.
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    String absoluteUrls = request.getParameter(ABSOLUTE);
    if(absoluteUrls != null) {
      LOG.debug("Applying absolute URL attribute for newsletter template generation.");
      request.setAttribute(ABSOLUTE, Boolean.parseBoolean(absoluteUrls));
    }
    filterChain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    //not required
  }
}
