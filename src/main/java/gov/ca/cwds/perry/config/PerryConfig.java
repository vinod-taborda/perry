package gov.ca.cwds.perry.config;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;

public class PerryConfig extends Configuration {

  /**
   * The application name
   */
  @NotEmpty
  private String applicationName;

  /**
   * The version
   */
  @NotEmpty
  private String version;

  private SwaggerConfiguration swaggerConfiguration;

  private DataSourceFactory nsDataSourceFactory;

  private FlywayFactory flywayFactory;

  private DataSourceFactory cmsDataSourceFactory;

  @JsonProperty
  public String getApplicationName() {
    return applicationName;
  }

  @JsonProperty
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  /**
   * @return the version
   */
  @JsonProperty
  public String getVersion() {
    return version;
  }

  /**
   * @param version the version to set
   */
  @JsonProperty
  public void setVersion(String version) {
    this.version = version;
  }

  @JsonProperty
  public DataSourceFactory getNsDataSourceFactory() {
    return nsDataSourceFactory;
  }

  @JsonProperty
  public void setNsDataSourceFactory(DataSourceFactory dataSourceFactory) {
    this.nsDataSourceFactory = dataSourceFactory;
  }

  @JsonProperty
  public DataSourceFactory getCmsDataSourceFactory() {
    return cmsDataSourceFactory;
  }

  @JsonProperty
  public void setDataSourceFactoryLegacy(DataSourceFactory dataSourceFactory) {
    this.cmsDataSourceFactory = dataSourceFactory;
  }

  @JsonProperty
  public FlywayFactory getFlywayFactory() {
    return flywayFactory;
  }

  @JsonProperty
  public void setFlywayFactory(FlywayFactory flywayFactory) {
    this.flywayFactory = flywayFactory;
  }

  @JsonProperty(value = "swagger")
  public SwaggerConfiguration getSwaggerConfiguration() {
    return swaggerConfiguration;
  }

  @JsonProperty
  public void setSwaggerConfiguration(SwaggerConfiguration swaggerConfiguration) {
    this.swaggerConfiguration = swaggerConfiguration;
  }

}
