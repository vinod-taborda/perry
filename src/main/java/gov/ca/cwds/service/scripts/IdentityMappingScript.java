package gov.ca.cwds.service.scripts;

import groovy.json.JsonOutput;
import java.io.IOException;
import javax.script.ScriptException;

/**
 * Created by dmitry.rudenko on 5/9/2017.
 */
public class IdentityMappingScript extends Script {
    public IdentityMappingScript(String path) throws IOException {
        super(path, "authorization");
    }

    public String map(Object userAuthorization) throws ScriptException {
        return JsonOutput.toJson(eval(userAuthorization));
    }
}
