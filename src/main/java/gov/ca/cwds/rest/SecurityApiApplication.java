package gov.ca.cwds.rest;

import gov.ca.cwds.inject.ApplicationModule;
import gov.ca.cwds.rest.resources.auth.LoginResourceHelper;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Module;

/**
 * Core execution class of CWDS REST API server application.
 * 
 * @author CWDS API Team
 */
public class SecurityApiApplication extends BaseApiApplication<SecurityApiConfiguration> {

  @SuppressWarnings("unused")
  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityApiApplication.class);

  public static void main(final String[] args) throws Exception {
    new SecurityApiApplication().run(args);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.ca.cwds.rest.BaseApiApplication#applicationModule(io.dropwizard.setup.Bootstrap)
   */
  @Override
  public Module applicationModule(Bootstrap<SecurityApiConfiguration> bootstrap) {
    return new ApplicationModule(bootstrap);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.ca.cwds.rest.BaseApiApplication#runInternal(gov.ca.cwds.rest.BaseApiConfiguration,
   * io.dropwizard.setup.Environment)
   */
  @Override
  public void runInternal(SecurityApiConfiguration configuration, Environment environment) {
    super.runInternal(configuration, environment);
    // ensure our login resource helper is setup
    guiceBundle.getInjector().getInstance(LoginResourceHelper.class);

    new AssetsBundle(configuration.getSimpleAccountLoginConfiguration().getAssetsPath(),
        configuration.getSimpleAccountLoginConfiguration().getAssetsPath(), null,
        "simpleAccountLogin").run(environment);
  }


}
