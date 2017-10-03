package gov.ca.cwds.service;

import gov.ca.cwds.PerryProperties;
import gov.ca.cwds.UniversalUserToken;
import org.junit.Test;

/**
 * Created by dmitry.rudenko on 10/2/2017.
 */
public class IdentityMappingServiceTest {
  @Test
  public void test() {
    IdentityMappingService identityMappingService = new IdentityMappingService();
    PerryProperties configuration = new PerryProperties();
    identityMappingService.setConfiguration(configuration);
    UniversalUserToken universalUserToken = new UniversalUserToken();
    universalUserToken.setUserId("user");
    String user = identityMappingService.map(universalUserToken, "test");
    assert user.equals("user");

  }
}
