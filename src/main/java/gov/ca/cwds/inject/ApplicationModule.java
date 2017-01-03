package gov.ca.cwds.inject;

import com.google.inject.AbstractModule;

import gov.ca.cwds.rest.SecurityApiConfiguration;
import io.dropwizard.setup.Bootstrap;

public class ApplicationModule extends AbstractModule {

  private Bootstrap<SecurityApiConfiguration> bootstrap;

  public ApplicationModule(Bootstrap<SecurityApiConfiguration> bootstrap) {
    this.bootstrap = bootstrap;
  }

  @Override
  protected void configure() {
    install(new ServicesModule());
    install(new ResourcesModule());
    install(new DataAccessModule(bootstrap));
    install(new FiltersModule());
    install(new AuditingModule());
  }

}
