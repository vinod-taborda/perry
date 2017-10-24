package gov.ca.cwds.service;

import gov.ca.cwds.UniversalUserToken;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
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
  private final AuthorizationCodeServices codeServices = new InMemoryAuthorizationCodeServices();

  @Autowired
  IdentityMappingService identityMappingService;
  @Autowired
  OAuth2ClientContext oauth2ClientContext;
  @Autowired
  SAFService safService;
  @Autowired
  TokenStore tokenStore;
  @Autowired
  TokenServiceImpl tokenService;

  public String login(String providerId) throws Exception {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    OAuth2Authentication authentication = (OAuth2Authentication)securityContext.getAuthentication();
    UniversalUserToken userToken = (UniversalUserToken) authentication.getPrincipal();
    OAuth2AccessToken accessToken = oauth2ClientContext.getAccessToken();
    String jwtIdentity = identityMappingService.map(userToken, providerId);
    accessToken.getAdditionalInformation().put(IDENTITY, jwtIdentity);
    tokenStore.storeAccessToken(accessToken, authentication);

    return codeServices.createAuthorizationCode(authentication);
  }

  @Override
  public String issueToken(String accessCode) {
    OAuth2Authentication authentication = codeServices.consumeAuthorizationCode(accessCode);
    String perryToken = UUID.randomUUID().toString();
    return  perryToken;
  }

  public String validate(String token) throws Exception {
    return tokenService.validate(token);
  }

}
