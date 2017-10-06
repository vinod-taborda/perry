package gov.ca.cwds.security;

import com.google.inject.Inject;
import gov.ca.cwds.security.authorizer.TestAuthorizer;
import gov.ca.cwds.security.module.SecurityModule;
import gov.ca.cwds.testapp.service.TestService;
import org.apache.shiro.authz.UnauthorizedException;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by dmitry.rudenko on 10/6/2017.
 */
public class ApiSecurityTest extends AbstractApiSecurityTest {

  @Inject
  TestService testService;


  @Before
  public void before() throws Exception {
    initInjector(new SecurityModule().addAuthorizer("case:read", TestAuthorizer.class));
  }

  @Test(expected = UnauthorizedException.class)
  public void testUnauthorized() throws Exception {
    testService.testArg("2");
  }

  @Test
  public void testAuthorized() throws Exception {
    testService.testArg("1");
  }
}
