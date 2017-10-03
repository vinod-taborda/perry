package gov.ca.cwds.service;

import gov.ca.cwds.UniversalUserToken;
import gov.ca.cwds.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContext;
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

  public String login(String providerId) throws Exception {
    SecurityContext context = SecurityContextHolder.getContext();
    UniversalUserToken userToken = (UniversalUserToken) context.getAuthentication().getPrincipal();
    String jwtIdentity = identityMappingService.map(userToken, providerId);
    return jwtService.generate(UUID.randomUUID().toString(), userToken.getUserId(), jwtIdentity);
  }

  public String validate(String token) throws Exception {
    return jwtService.validate(token);
  }
}
