package gov.ca.cwds.service.oauth;

import gov.ca.cwds.PerryProperties;
import gov.ca.cwds.UniversalUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.script.ScriptException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by dmitry.rudenko on 7/28/2017.
 */
@Component
@Primary
@Profile("prod")
public class UniversalUserTokenExtractor implements PrincipalExtractor {
  private PerryProperties configuration;

  @Override
  public UniversalUserToken extractPrincipal(Map<String, Object> map) {
    try {
      UniversalUserToken userToken = configuration.getIdentityProvider().getIdpMapping().map(map);
      userToken.setToken(generateToken());
      return userToken;
    } catch (ScriptException e) {
      throw new RuntimeException(e);
    }
  }

  private String generateToken() {
    return UUID.randomUUID().toString();
  }

  @Autowired
  public void setConfiguration(PerryProperties perryProperties) {
    this.configuration = perryProperties;
  }
}
