package gov.ca.cwds.testapp.service;

import gov.ca.cwds.security.annotations.Authorize;

/**
 * Created by dmitry.rudenko on 10/6/2017.
 */
public class TestServiceImpl implements TestService {
  @Authorize("case:read:&arg0")
  public void testArg(String id) {

  }
}
