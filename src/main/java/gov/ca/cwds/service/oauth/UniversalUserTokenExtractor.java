package gov.ca.cwds.service.oauth;

import gov.ca.cwds.PerryProperties;
import gov.ca.cwds.UniversalUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.util.Map;

/**
 * Created by dmitry.rudenko on 7/28/2017.
 */
@Service
@Profile("prod")
public class UniversalUserTokenExtractor implements PrincipalExtractor {
  @Autowired
  private PerryProperties configuration;

  @Override
  public UniversalUserToken extractPrincipal(Map<String, Object> map) {
    try {
      return configuration.getIdentityProvider().getIdpMapping().map(map);
    } catch (ScriptException e) {
      throw new RuntimeException(e);
    }
  }
}
