package gov.ca.cwds.service.scripts;

import org.junit.Test;

/**
 * Created by leonid.marushevskyi on 1/3/2018.
 */
public class DefaultMappingTest extends BaseScriptTest {
  @Test
  public void testGroovyMapping() throws Exception {

    test("/scripts/default/default.groovy", "/scripts/default/default.json", "scripts/default/authz.json");
  }
}
