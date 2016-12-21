package gov.ca.cwds.perry.inject;

import com.google.inject.AbstractModule;

/**
 * DI for Filter classes
 * 
 * @author CWDS API Team
 */
public class FiltersModule extends AbstractModule {

  @Override
  protected void configure() {
    // bind(RequestResponseLoggingFilter.class);
  }

}
