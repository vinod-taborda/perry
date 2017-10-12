package gov.ca.cwds.security.authorizer;

import org.apache.shiro.authz.AuthorizationException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by TPT2 on 10/12/2017.
 */
public class BaseAuthorizerTest {
  private static BaseAuthorizer<String, Long> authorizer;

  @BeforeClass
  public static void before() {
    authorizer = new BaseAuthorizer<String, Long>() {
    };
  }

  @Test(expected = AuthorizationException.class)
  public void testCheckIdNotImplemented() {
    authorizer.checkId(1L);
  }

  @Test(expected = AuthorizationException.class)
  public void testCheckInstanceNotmplemented() {
    authorizer.checkInstance("");
  }

  @Test(expected = AuthorizationException.class)
  public void testCheckStringToIdNotImplemented() {
    authorizer.stringToId("1");
  }
}
