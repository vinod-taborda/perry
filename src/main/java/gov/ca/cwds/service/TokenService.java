package gov.ca.cwds.service;

import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

/**
 * @author CWDS CALS API Team
 */
@Profile("prod")
@Service
@Transactional
public class TokenService {

  private static final String IDENTITY = "identity";

  @Autowired
  SAFService safService;
  @Autowired
  TokenStore tokenStore;

  public void invalidate(String token) {
    revokeToken(token);
    safService.invalidate(token);
  }


  public String validate(String token) {
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
    if (accessToken != null) {
      Map response = safService.validate(token);
      if (!Boolean.parseBoolean(String.valueOf(response.get("active")))) {
        revokeToken(token);
        throw new IllegalStateException("The token is not in active state");
      }
      return (String) accessToken.getAdditionalInformation().get(IDENTITY);
    } else {
      throw new IllegalStateException("There is no accessToken in tokenStore for value:" + token);
    }
  }

  private void revokeToken(String token) {
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
    if(accessToken == null) {
      throw new IllegalArgumentException("There is no AccessToken for value: " + token);
    }
    tokenStore.removeAccessToken(accessToken);
  }
}
