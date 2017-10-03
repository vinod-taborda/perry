package gov.ca.cwds.service;

import gov.ca.cwds.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author CWDS TPT-2 Team
 */
@Service
@Profile("dev")
public class TokenServiceDev implements TokenService {
  @Autowired
  JwtService jwtService;
  @Override
  public void invalidate(String token) {
     // For development purposes 
  }

  @Override
  public String validate(String token) {
    return jwtService.validate(token);
  }
}
