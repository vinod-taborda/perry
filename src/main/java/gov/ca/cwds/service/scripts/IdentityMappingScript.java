package gov.ca.cwds.service.scripts;

import gov.ca.cwds.UniversalUserToken;
import gov.ca.cwds.rest.api.domain.auth.UserAuthorization;
import groovy.json.JsonOutput;
import java.io.IOException;
import javax.script.ScriptException;

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
