package gov.ca.cwds.service.scripts;

import gov.ca.cwds.UniversalUserToken;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by dmitry.rudenko on 7/28/2017.
 */
@SuppressWarnings("unchecked")
public class IdpMappingScriptTest {
  @Test
  public void testGroovyMapping() throws Exception {
    String path = Paths.get(getClass().getResource("/idp.groovy").toURI()).toString();
    IdpMappingScript idpMappingScript = new IdpMappingScript(path);
    Map userInfo = new HashMap();
    userInfo.put("safid.racfid", "racfid");
    userInfo.put("ok", "true");
    UniversalUserToken userToken = idpMappingScript.map(userInfo);
    assertEquals("racfid", userToken.getUserId());
  }
}
