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
 * Created by dmitry.rudenko on 5/22/2017.
 */
@Profile("prod")
@Service
@Transactional
public class OauthLoginService implements LoginService {

  private static final String IDENTITY = "identity";

  @Autowired
  IdentityMappingService identityMappingService;
  @Autowired
  OAuth2ClientContext oauth2ClientContext;
  @Autowired
  SAFService safService;
  @Autowired
  TokenStore tokenStore;

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
    safService.validate(token);
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
    return (String) accessToken.getAdditionalInformation().get(IDENTITY);
  }

}
