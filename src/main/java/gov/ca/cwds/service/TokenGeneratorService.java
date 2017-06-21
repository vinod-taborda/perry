package gov.ca.cwds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by dmitry.rudenko on 6/15/2017.
 */
@Service
@Profile("dev")
public class TokenGeneratorService {
  @Autowired
  private JwtService jwtService;

  public String generate(String identity) throws Exception {
    return jwtService.generateToken("test", UUID.randomUUID().toString(), identity);
  }
}
