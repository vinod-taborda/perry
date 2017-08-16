package gov.ca.cwds.service.scripts;

import gov.ca.cwds.rest.api.domain.auth.UserAuthorization;
import static org.junit.Assert.*;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Created by dmitry.rudenko on 7/25/2017.
 */
public class IdentityMappingScriptTest extends BaseScriptTest {
  @Test
  public void testGroovyMapping() throws Exception {

    test("/scripts/basic/test.groovy", "/scripts/basic/test.json", "scripts/basic/authz.json");
  }
}
