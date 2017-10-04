package gov.ca.cwds.service;

import gov.ca.cwds.security.jwt.JwtService;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by dmitry.rudenko on 10/3/2017.
 */
public class FormLoginServiceTest {

  public static final String TOKEN = "token";
  public static final String IDENTITY = "identity";
  public static final String USER = "user";

  @Test
  public void test() throws Exception {
    FormLoginService formLoginService = new FormLoginService();
    TokenGeneratorService tokenGeneratorService = Mockito.mock(TokenGeneratorService.class);
    Mockito.when(tokenGeneratorService.generate(USER)).thenReturn(TOKEN);
    formLoginService.setTokenGeneratorService(tokenGeneratorService);
    JwtService jwtService = Mockito.mock(JwtService.class);
    Mockito.when(jwtService.validate(TOKEN)).thenReturn(IDENTITY);
    formLoginService.setJwtService(jwtService);
    Authentication authentication = new UsernamePasswordAuthenticationToken(USER, "password");
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = formLoginService.login("spId");
    String identity = formLoginService.validate(TOKEN);
    assert identity.equals(IDENTITY);
    assert  token.equals(TOKEN);

  }
}
