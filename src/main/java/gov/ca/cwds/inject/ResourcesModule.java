package gov.ca.cwds.inject;

import javax.ws.rs.client.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import gov.ca.cwds.rest.SAFConfiguration;
import gov.ca.cwds.rest.SecurityApiConfiguration;
import gov.ca.cwds.rest.SimpleAccountLoginConfiguration;
import gov.ca.cwds.rest.SwaggerConfiguration;
import gov.ca.cwds.rest.api.ApiException;
import gov.ca.cwds.rest.resources.ApplicationResource;
import gov.ca.cwds.rest.resources.ResourceDelegate;
import gov.ca.cwds.rest.resources.ServiceBackedResourceDelegate;
import gov.ca.cwds.rest.resources.SwaggerResource;
import gov.ca.cwds.rest.resources.auth.LoginResourceHelper;
import gov.ca.cwds.rest.resources.auth.SimpleAccountLoginResourceHelper;
import gov.ca.cwds.rest.resources.auth.UserAuthorizationResource;
import gov.ca.cwds.rest.resources.auth.saf.OauthLoginResource;
import gov.ca.cwds.rest.services.auth.UserAuthorizationService;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;

public class ResourcesModule extends AbstractModule {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesModule.class);

  private Client client;

  @Override
  protected void configure() {
    bind(ApplicationResource.class);
    bind(SwaggerResource.class);
    bind(UserAuthorizationResource.class);
    bind(OauthLoginResource.class);
    bind(SimpleAccountLoginResourceHelper.class);
  }

  @Provides
  public SwaggerConfiguration swaggerConfiguration(SecurityApiConfiguration apiConfiguration) {
    return apiConfiguration.getSwaggerConfiguration();
  }

  @Provides
  public SAFConfiguration safConfiguration(SecurityApiConfiguration apiConfiguration) {
    return apiConfiguration.getSafConfiguration();
  }

  @Provides
  public SimpleAccountLoginConfiguration simpleAccountLoginConfiguration(
      SecurityApiConfiguration apiConfiguration) {
    return apiConfiguration.getSimpleAccountLoginConfiguration();
  }

  @Provides
  LoginResourceHelper loginResourceHelper(SecurityApiConfiguration apiConfiguration,
      Injector injector) {
    try {
      LOGGER.info("LoginResourceHelper bound to {}", apiConfiguration.getLoginResourceHelper());
      return (LoginResourceHelper) injector
          .getInstance(Class.forName(apiConfiguration.getLoginResourceHelper()));
    } catch (ClassNotFoundException e) {
      throw new ApiException("Unable to create login resource helper", e);
    }
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

  @Provides
  public Client client(SecurityApiConfiguration apiConfiguration, Environment environment) {
    if (client == null) {
      client = new JerseyClientBuilder(environment)
          .using(apiConfiguration.getJerseyClientConfiguration()).build("client");
    }
    return client;
  }
}
