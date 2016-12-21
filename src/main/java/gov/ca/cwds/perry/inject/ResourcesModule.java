package gov.ca.cwds.perry.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import gov.ca.cwds.perry.config.PerryConfig;
import gov.ca.cwds.perry.config.SwaggerConfiguration;


public class ResourcesModule extends AbstractModule {


  public ResourcesModule() {}

  @Override
  protected void configure() {
    // bind(ApplicationResource.class);
  }

  @Provides
  public SwaggerConfiguration swaggerConfiguration(PerryConfig apiConfiguration) {
    return apiConfiguration.getSwaggerConfiguration();
  }

  @Provides
  @Named("app.name")
  public String appName(PerryConfig apiConfiguration) {
    return apiConfiguration.getApplicationName();
  }

  @Provides
  @Named("app.version")
  public String appVersion(PerryConfig apiConfiguration) {
    return apiConfiguration.getVersion();
  }

  // @Provides
  // @AddressServiceBackedResource
  // public ResourceDelegate addressServiceBackedResource(Injector injector) {
  // return new ServiceBackedResourceDelegate(injector.getInstance(AddressService.class));
  // }
  //
  // @Provides
  // @CmsDocumentReferralClientServiceBackedResource
  // public ResourceDelegate cmsDocumentReferralClientServiceBackedResource(Injector injector) {
  // return new ServiceBackedResourceDelegate(
  // injector.getInstance(CmsDocReferralClientService.class));
  // }


}
