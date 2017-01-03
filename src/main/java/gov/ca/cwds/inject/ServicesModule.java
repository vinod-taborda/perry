package gov.ca.cwds.inject;

import com.google.inject.AbstractModule;

import gov.ca.cwds.rest.services.auth.UserAuthorizationService;

public class ServicesModule extends AbstractModule {
  public ServicesModule() {}

  @Override
  protected void configure() {
    bind(UserAuthorizationService.class);
  }

}
