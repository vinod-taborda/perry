package gov.ca.cwds.service;

import static gov.ca.cwds.security.jwt.JwtService.IDENTITY_CLAIM;

import gov.ca.cwds.UniversalUserToken;
import gov.ca.cwds.security.jwt.JwtService;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@Profile("prod")
@Service
@Transactional
public class OauthLoginService implements LoginService {

  private static final String ACCESS_TOKEN_CLAIM = "accessToken";

  @Autowired
  IdentityMappingService identityMappingService;
  @Autowired
  JwtService jwtService;
  @Autowired
  OAuth2ClientContext oauth2ClientContext;

  public String login(String providerId) throws Exception {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    UniversalUserToken userToken = (UniversalUserToken) securityContext
        .getAuthentication().getPrincipal();

    OAuth2AccessToken accessToken = oauth2ClientContext.getAccessToken();

    String jwtIdentity = identityMappingService.map(userToken, providerId);

    HashMap<String, String> customJwtClaimsMap = new HashMap<>();
    customJwtClaimsMap.put(IDENTITY_CLAIM, jwtIdentity);
    customJwtClaimsMap.put(ACCESS_TOKEN_CLAIM, accessToken.getValue());

    return jwtService
        .generate(UUID.randomUUID().toString(), userToken.getUserId(), customJwtClaimsMap);
  }

  public String validate(String token) throws Exception {
    return jwtService.validate(token);
  }
}
