package gov.ca.cwds.service;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * Created by dmitry.rudenko on 10/3/2017.
 */
public class OauthLogoutHandlerTest {

  public static final String TOKEN = "token";

  @Test
  public void test() {
    OauthLogoutHandler oauthLogoutHandler = new OauthLogoutHandler();
    TokenService tokenService = Mockito.mock(TokenService.class);
    TokenStore tokenStore = Mockito.mock(TokenStore.class);
    OAuth2AccessToken oAuth2AccessToken = Mockito.mock(OAuth2AccessToken.class);
    oauthLogoutHandler.setTokenService(tokenService);
    oauthLogoutHandler.setTokenStore(tokenStore);
    OAuth2Authentication authentication = Mockito.mock(OAuth2Authentication.class);
    Mockito.when(oAuth2AccessToken.getValue()).thenReturn(TOKEN);
    Mockito.when(tokenStore.getAccessToken(authentication)).thenReturn(oAuth2AccessToken);
    oauthLogoutHandler.logout(null, null, authentication);
    Mockito.verify(tokenService).invalidate(TOKEN);
  }
}
