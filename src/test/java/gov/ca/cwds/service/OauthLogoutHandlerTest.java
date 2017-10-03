package gov.ca.cwds.service;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * Created by dmitry.rudenko on 10/3/2017.
 */
public class OauthLogoutHandlerTest {

  @Test
  public void test() {
    OauthLogoutHandler oauthLogoutHandler = new OauthLogoutHandler();
    TokenService tokenService = Mockito.mock(TokenService.class);
    oauthLogoutHandler.setTokenService(tokenService);
    OAuth2Authentication authentication = Mockito.mock(OAuth2Authentication.class);
    oauthLogoutHandler.logout(null, null, authentication);
    Mockito.verify(tokenService).invalidate(authentication);
  }
}
