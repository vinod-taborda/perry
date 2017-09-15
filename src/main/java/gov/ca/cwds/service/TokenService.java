package gov.ca.cwds.service;

import java.util.Map;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

/**
 * @author CWDS CALS API Team
 */
@Profile("prod")
@Service
@Transactional
public class TokenService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

  private static final String IDENTITY = "identity";

  @Autowired
  SAFService safService;
  @Autowired
  TokenStore tokenStore;

  public void invalidate(String token) {
    revokeToken(token);
    try {
      String result = safService.invalidate(token);
      LOGGER.info("SAFToken invalidation result: %s", result);
    } catch (Exception e) {
      LOGGER.warn("OAuth token invalidation problems", e);
    }
  }


  public String validate(String token) {
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
    if (accessToken != null) {
      try {
        Map response = null;
        response = safService.validate(token);
        if (!Boolean.parseBoolean(String.valueOf(response.get("active")))) {
          revokeToken(token);
          throw new IllegalStateException("The token is not in active state");
        }
        return (String) accessToken.getAdditionalInformation().get(IDENTITY);
      } catch (SAFServiceException e) {
        LOGGER.error("Validation problem on SAF ", e);
        throw new IllegalStateException(e);
      }
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

  public void invalidate(OAuth2Authentication authentication) {
    OAuth2AccessToken accessToken = tokenStore.getAccessToken(authentication);
    if (accessToken == null) {
      throw new IllegalStateException("There is no accessToken for authentication: "
          + authentication.getUserAuthentication().getName() );
    }
    invalidate(accessToken.getValue());
  }
}
