package gov.ca.cwds.service.scripts;

import gov.ca.cwds.rest.api.domain.auth.UserAuthorization;
import static org.junit.Assert.*;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Created by dmitry.rudenko on 7/25/2017.
 */
public class IdentityMappingScriptTest {
  @Test
  public void testGroovyMapping() throws Exception {
    String path =  Paths.get(getClass().getResource("/test.groovy").toURI()).toString();
    IdentityMappingScript identityMappingScript = new IdentityMappingScript(path);
    UserAuthorization userAuthorization = new UserAuthorization("userId",
            "staffId",
            null,
            null,
            null,
            null,
            null,
            null);
    String json = identityMappingScript.map(userAuthorization);
    assertEquals("{\"user\":\"userId\",\"roles\":[\"none\"],\"staffId\":\"staffId\"}", json);

  }
}
