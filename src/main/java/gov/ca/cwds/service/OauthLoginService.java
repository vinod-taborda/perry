package gov.ca.cwds.service;

import gov.ca.cwds.UniversalUserToken;
import gov.ca.cwds.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@Profile("prod")
@Service
@Primary
@Transactional
public class OauthLoginService implements LoginService {
  @Autowired
  IdentityMappingService identityMappingService;
  @Autowired
  JwtService jwtService;
  @Autowired
  TokenLoginService tokenLoginService;

  public String login(String providerId) throws Exception {
    UniversalUserToken userToken = (UniversalUserToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String jwtIdentity = identityMappingService.map(userToken, providerId);
    return jwtService.generate(UUID.randomUUID().toString(), userToken.getUserId(), jwtIdentity);
  }

  public String validate(String token) throws Exception {
    return jwtService.validate(token);
  }

  public String loginV2(String providerId) throws Exception {
    return tokenLoginService.login(providerId);
  }

  public String validateV2(String token) throws Exception {
    return tokenLoginService.validate(token);
  }

}
