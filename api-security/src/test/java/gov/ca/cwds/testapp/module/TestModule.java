package gov.ca.cwds.testapp.module;

import com.google.inject.AbstractModule;
import gov.ca.cwds.testapp.service.TestService;
import gov.ca.cwds.testapp.service.TestServiceImpl;

/**
 * Created by dmitry.rudenko on 10/6/2017.
 */
public class TestModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(TestService.class).to(TestServiceImpl.class);
  }
}
