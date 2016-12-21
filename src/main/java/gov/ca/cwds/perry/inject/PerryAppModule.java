package gov.ca.cwds.perry.inject;

import com.google.inject.AbstractModule;

import gov.ca.cwds.perry.config.PerryConfig;
import io.dropwizard.setup.Bootstrap;

public class PerryAppModule extends AbstractModule {

  private Bootstrap<PerryConfig> bootstrap;

  public PerryAppModule(Bootstrap<PerryConfig> bootstrap) {
    this.bootstrap = bootstrap;
  }

  @Override
  protected void configure() {
    install(new ServicesModule());
    install(new ResourcesModule());
    install(new FiltersModule());
  }

  public Bootstrap<PerryConfig> getBootstrap() {
    return bootstrap;
  }

}
