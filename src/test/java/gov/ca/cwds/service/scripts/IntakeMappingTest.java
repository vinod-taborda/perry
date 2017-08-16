package gov.ca.cwds.service.scripts;

import org.junit.Test;

/**
 * Created by dmitry.rudenko on 8/16/2017.
 */
public class IntakeMappingTest extends BaseScriptTest {
  @Test
  public void testGroovyMapping() throws Exception {

    test("/scripts/intake/intake.groovy", "/scripts/intake/intake.json", "scripts/intake/authz.json");
  }
}
