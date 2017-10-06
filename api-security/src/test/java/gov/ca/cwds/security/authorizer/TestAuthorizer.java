package gov.ca.cwds.security.authorizer;

/**
 * Created by dmitry.rudenko on 10/6/2017.
 */

public class TestAuthorizer implements Authorizer {
  @Override
  public boolean check(String id) {
    return id.equals("1");
  }
}
