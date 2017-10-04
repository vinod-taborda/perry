package gov.ca.cwds.service;

import gov.ca.cwds.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by dmitry.rudenko on 5/23/2017.
 */
@Profile("dev")
@Service
public class FormLoginService implements LoginService {

  private TokenGeneratorService tokenGeneratorService;

  private JwtService jwtService;

  @Override
  @SuppressWarnings("unchecked")
  public String login(String providerId) throws Exception {
    UsernamePasswordAuthenticationToken authenticationToken =
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    String username = authenticationToken.getName();
    return tokenGeneratorService.generate(username);
  }

  @Override
  public String validate(String token) throws Exception {
    return jwtService.validate(token);
  }

  @Autowired
  public void setTokenGeneratorService(TokenGeneratorService tokenGeneratorService) {
    this.tokenGeneratorService = tokenGeneratorService;
  }

  @Autowired
  public void setJwtService(JwtService jwtService) {
    this.jwtService = jwtService;
  }
}
