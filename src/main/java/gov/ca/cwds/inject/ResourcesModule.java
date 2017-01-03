package gov.ca.cwds.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import gov.ca.cwds.rest.SecurityApiConfiguration;
import gov.ca.cwds.rest.SwaggerConfiguration;
import gov.ca.cwds.rest.resources.ApplicationResource;
import gov.ca.cwds.rest.resources.ResourceDelegate;
import gov.ca.cwds.rest.resources.ServiceBackedResourceDelegate;
import gov.ca.cwds.rest.resources.SwaggerResource;
import gov.ca.cwds.rest.resources.auth.UserAuthorizationResource;
import gov.ca.cwds.rest.services.auth.UserAuthorizationService;

public class ResourcesModule extends AbstractModule {

  public ResourcesModule() {}

  @Override
  protected void configure() {
    bind(ApplicationResource.class);
    bind(SwaggerResource.class);
    bind(UserAuthorizationResource.class);
  }

  @Provides
  public SwaggerConfiguration swaggerConfiguration(SecurityApiConfiguration apiConfiguration) {
    return apiConfiguration.getSwaggerConfiguration();
  }

  @Provides
  @Named("app.name")
  public String appName(SecurityApiConfiguration apiConfiguration) {
    return apiConfiguration.getApplicationName();
  }

  @Provides
  @Named("app.version")
  public String appVersion(SecurityApiConfiguration apiConfiguration) {
    return apiConfiguration.getVersion();
  }

  @Provides
  @UserAuthorizationServiceBackedResource
  public ResourceDelegate userAuthorizationServiceBackedResource(Injector injector) {
    return new ServiceBackedResourceDelegate(injector.getInstance(UserAuthorizationService.class));
  }
}
