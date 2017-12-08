package gov.ca.cwds.security.realm;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.shiro.authc.AuthenticationException;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author CWDS TPT-2 Team
 */
public class AbstractRealmTest {

  @Test
  public void realmMapTest() throws Exception {
    AbstractRealm realm = new AbstractRealm() {
      @Override
      protected String validate(String token) throws AuthenticationException {
        return null;
      }
    };
    realm.onInit();

    String token = new String(
        Files.readAllBytes(Paths.get(getClass().getResource("/security/token.json").toURI())));
    PerryAccount perryAccount = realm.map(token);
    Assert.assertEquals("testuser", perryAccount.getUser());
    Assert.assertTrue(perryAccount.getRoles() != null && perryAccount.getRoles().size() == 2);

    String tokenExt = new String(
        Files.readAllBytes(
            Paths.get(getClass().getResource("/security/token-extended.json").toURI())));
    PerryAccount perryAccountExt = realm.map(tokenExt);
    Assert.assertEquals("RACFID", perryAccountExt.getUser());
    Assert.assertEquals("34", perryAccountExt.getStaffId());
    Assert.assertTrue(perryAccountExt.getRoles() != null && perryAccountExt.getRoles().size() == 1);
    Assert.assertEquals("19", perryAccountExt.getCountyCode());
    Assert.assertEquals("Los Angeles", perryAccountExt.getCountyName());
    Assert.assertTrue(
        perryAccountExt.getPrivileges() != null && perryAccountExt.getPrivileges().size() == 2);
  }

}