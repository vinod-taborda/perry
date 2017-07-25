package gov.ca.cwds.security.shiro;

import org.junit.Test;

/**
 * Created by dmitry.rudenko on 7/25/2017.
 */
public class PerryShiroTokenTest {

  @Test(expected = AuthenticationException.class)
  public void testNullToken() {
    new PerryShiroToken(null);
  }
}
