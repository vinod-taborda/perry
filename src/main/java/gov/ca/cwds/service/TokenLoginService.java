package gov.ca.cwds.service;

import gov.ca.cwds.UniversalUserToken;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
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
public class TokenLoginService implements LoginService {

  private static final String IDENTITY = "identity";

  @Autowired
  IdentityMappingService identityMappingService;
  @Autowired
  OAuth2ClientContext oauth2ClientContext;
  @Autowired
  SAFService safService;
  @Autowired
  TokenStore tokenStore;
  @Autowired
  TokenService tokenService;

  public String login(String providerId) throws Exception {
    SecurityContext securityContext = SecurityContextHolder.getContext();

    OAuth2Authentication authentication = (OAuth2Authentication)securityContext.getAuthentication();
    UniversalUserToken userToken = (UniversalUserToken) authentication.getPrincipal();
    OAuth2AccessToken accessToken = oauth2ClientContext.getAccessToken();
    String jwtIdentity = identityMappingService.map(userToken, providerId);
    accessToken.getAdditionalInformation().put(IDENTITY, jwtIdentity);
    tokenStore.storeAccessToken(accessToken, authentication);
    return accessToken.getValue();
  }

  public String validate(String token) throws Exception {
    return tokenService.validate(token);
  }

}
