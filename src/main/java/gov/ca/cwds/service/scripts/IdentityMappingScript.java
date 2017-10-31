package gov.ca.cwds.service.scripts;

import gov.ca.cwds.UniversalUserToken;
import groovy.json.JsonOutput;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * Created by dmitry.rudenko on 5/9/2017.
 */
public class IdentityMappingScript extends Script {
  public IdentityMappingScript(String path) throws IOException {
    super(path, "user");
  }

  public String map(UniversalUserToken user) throws ScriptException {
    return JsonOutput.toJson(eval(user));
  }
}
