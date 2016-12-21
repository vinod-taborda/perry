package gov.ca.cwds.perry.config;

import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.flywaydb.core.Flyway;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hubspot.dropwizard.guice.GuiceBundle;

import gov.ca.cwds.perry.inject.PerryAppModule;
import gov.ca.cwds.perry.resource.SwaggerResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

/**
 * Core execution class of CWDS Access Security REST API server application, aka., Perry.
 * 
 * @author CWDS API Team
 */
public class PerryApp extends Application<PerryConfig> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PerryApp.class);

  private GuiceBundle<PerryConfig> guiceBundle;

  private final FlywayBundle<PerryConfig> flywayBundle = new FlywayBundle<PerryConfig>() {
    @Override
    public DataSourceFactory getDataSourceFactory(PerryConfig configuration) {
      return configuration.getNsDataSourceFactory();
    }

    @Override
    public FlywayFactory getFlywayFactory(PerryConfig configuration) {
      return configuration.getFlywayFactory();
    }

  };

  public static void main(final String[] args) throws Exception {
    new PerryApp().run(args);
  }

  @Override
  public void initialize(Bootstrap<PerryConfig> bootstrap) {
    bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
        bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));

    bootstrap.addBundle(new ViewBundle<PerryConfig>());

    guiceBundle = GuiceBundle.<PerryConfig>newBuilder().addModule(new PerryAppModule(bootstrap))
        .setConfigClass(PerryConfig.class).enableAutoConfig(getClass().getPackage().getName())
        .build();

    bootstrap.addBundle(guiceBundle);
    bootstrap.addBundle(flywayBundle);
  }

  private void migrateDatabase(final PerryConfig configuration) {
    Flyway flyway = new Flyway();
    flyway.setDataSource(flywayBundle.getDataSourceFactory(configuration).build(null, "ds"));
    List<String> locations = flywayBundle.getFlywayFactory(configuration).getLocations();
    flyway.setLocations(locations.toArray(new String[locations.size()]));
    flyway.setSqlMigrationPrefix(
        flywayBundle.getFlywayFactory(configuration).getSqlMigrationPrefix());
    flyway.migrate();
  }

  @Override
  public void run(final PerryConfig configuration, final Environment environment) throws Exception {
    environment.jersey().getResourceConfig().packages(getClass().getPackage().getName())
        .register(DeclarativeLinkingFeature.class);

    environment.servlets().setSessionHandler(new SessionHandler());

    LOGGER.info("Application name: {}", configuration.getApplicationName());
    PerryEnv apiEnvironment = new PerryEnv(environment);

    LOGGER.info("Migrating Database");
    migrateDatabase(configuration);

    LOGGER.info("Configuring CORS: Cross-Origin Resource Sharing");
    configureCors(apiEnvironment);

    LOGGER.info("Configuring SWAGGER");
    configureSwagger(configuration, apiEnvironment);

    LOGGER.info("Registering Filters");
    registerFilters(environment);
  }

  public void registerFilters(final Environment environment) {}

  protected void configureCors(final PerryEnv apiEnvironment) {
    FilterRegistration.Dynamic filter =
        apiEnvironment.servlets().addFilter("CORS", CrossOriginFilter.class);
    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
    filter.setInitParameter("allowedHeaders",
        "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,X-Auth-Token");
    filter.setInitParameter("allowCredentials", "true");
  }

  protected void configureSwagger(final PerryConfig perryConfig,
      final PerryEnv apiEnvironment) {
    BeanConfig config = new BeanConfig();
    config.setTitle(perryConfig.getSwaggerConfiguration().getTitle());
    config.setDescription(perryConfig.getSwaggerConfiguration().getDescription());
    config.setResourcePackage(perryConfig.getSwaggerConfiguration().getResourcePackage());
    config.setScan(true);

    new AssetsBundle(perryConfig.getSwaggerConfiguration().getAssetsPath(),
        perryConfig.getSwaggerConfiguration().getAssetsPath(), null, "swagger")
            .run(apiEnvironment.environment());
    apiEnvironment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    apiEnvironment.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    LOGGER.info("Registering ApiListingResource");
    apiEnvironment.jersey().register(new ApiListingResource());

    LOGGER.info("Registering SwaggerResource");
    final SwaggerResource swaggerResource =
        new SwaggerResource(perryConfig.getSwaggerConfiguration());
    apiEnvironment.jersey().register(swaggerResource);
  }
}
