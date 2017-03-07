package gov.ca.cwds.inject;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.spi.ExecutorServiceProvider;
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
import gov.ca.cwds.rest.resources.auth.saf.SafLoginResourceHelper;
import gov.ca.cwds.rest.services.auth.UserAuthorizationService;
import io.dropwizard.jersey.jackson.JacksonMessageBodyProvider;
import io.dropwizard.jersey.validation.HibernateValidationFeature;
import io.dropwizard.setup.Environment;

public class ResourcesModule extends AbstractModule {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesModule.class);

  private Client client;

  @Override
  protected void configure() {
    bind(ApplicationResource.class);
    bind(SwaggerResource.class);
    bind(UserAuthorizationResource.class);
    bind(SafLoginResourceHelper.class);
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

      // NOTE : There are issues with the connector provider when configuring with
      // JerseyClientBuilder.
      // I've tried .using(myownconnectorprovider) but haven't yet figured out what the issue is to
      // fix the problem. Work around is below - will just use default connector provider.
      // client = new JerseyClientBuilder(environment)
      // .using(apiConfiguration.getJerseyClientConfiguration())
      // .build("client");

      // NOTE: Mimicing what JerseyClientBuilder is doing below except for making a connector
      // provider.
      // this should be fine for R1.
      ClientConfig config = new ClientConfig();
      config.register(new JacksonMessageBodyProvider(environment.getObjectMapper()));
      config.register(new HibernateValidationFeature(environment.getValidator()));

      ExecutorService executorService =
          environment.lifecycle().executorService("jersey-client-client-%d")
              .minThreads(apiConfiguration.getJerseyClientConfiguration().getMinThreads())
              .maxThreads(apiConfiguration.getJerseyClientConfiguration().getMinThreads())
              .workQueue(new ArrayBlockingQueue<>(
                  apiConfiguration.getJerseyClientConfiguration().getWorkQueueSize()))
              .build();

      ExecutorServiceProvider executorServiceProvider = new ExecutorServiceProvider() {
        private final ExecutorService threadPool = executorService;

        @Override
        public ExecutorService getExecutorService() {
          return this.threadPool;
        }

        @Override
        public void dispose(ExecutorService executorService) {}
      };

      config.register(executorServiceProvider);
      // HttpClientBuilder apacheHttpClientBuilder = new HttpClientBuilder(environment);
      // final ConfiguredCloseableHttpClient apacheHttpClient =
      // apacheHttpClientBuilder.buildWithDefaultRequestConfiguration("client");
      //
      //
      //
      // ConnectorProvider connectorProvider =
      // (client, runtimeConfig) -> new DropwizardApacheConnector(apacheHttpClient.getClient(),
      // apacheHttpClient.getDefaultRequestConfig(),
      // apiConfiguration.getJerseyClientConfiguration().isChunkedEncodingEnabled());
      // config.connectorProvider(connectorProvider);
      client = ClientBuilder.newClient(config);
      client.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "FINEST");



      System.out.println("FOO");
    }
    return client;
  }
}
