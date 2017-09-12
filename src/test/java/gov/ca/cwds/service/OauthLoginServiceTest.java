package gov.ca.cwds.service;

import static gov.ca.cwds.service.OauthLoginServiceTestConf.TOKEN;
import static gov.ca.cwds.service.OauthLoginServiceTestConf.TOKEN2;
import static gov.ca.cwds.service.OauthLoginServiceTestConf.TOKEN_EXPIRED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import gov.ca.cwds.UniversalUserToken;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author CWDS CALS API Team
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = OauthLoginServiceTestConf.class)
public class OauthLoginServiceTest {

  private static final String USER_ID = "userId";
  private static final String USER_ID1 = "userId1";
  private static final String USER_ID2 = "userId2";

  public static final DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(TOKEN);
  public static final DefaultOAuth2AccessToken oAuth2AccessTokenExpired = new DefaultOAuth2AccessToken(TOKEN_EXPIRED);
  public static final DefaultOAuth2AccessToken oAuth2AccessToken2 = new DefaultOAuth2AccessToken(TOKEN2);
  {
    oAuth2AccessToken.setAdditionalInformation(new HashMap<>());
    oAuth2AccessTokenExpired.setAdditionalInformation(new HashMap<>());
    oAuth2AccessToken2.setAdditionalInformation(new HashMap<>());
  }

  @Autowired
  private OAuth2ClientContext clientContext;

  @Autowired
  private OauthLoginService oauthLoginService;

  @Autowired
  private TokenStore tokenStore;

  SecurityContextImpl securityContext = new SecurityContextImpl();
  OAuth2Authentication authentication;
  OAuth2Authentication authentication1;
  OAuth2Authentication authentication2;

  {
    SecurityContextHolder.setContext(securityContext);

    UniversalUserToken universalUserToken = new UniversalUserToken();
    universalUserToken.setUserId(USER_ID);

    UniversalUserToken universalUserToken1 = new UniversalUserToken();
    universalUserToken1.setUserId(USER_ID1);

    UniversalUserToken universalUserToken2 = new UniversalUserToken();
    universalUserToken2.setUserId(USER_ID2);


    OAuth2Request request = new OAuth2Request(null, "clientId", null, true, null,
        null, null, null, null);

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        universalUserToken, "N/A", null);
    UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(
        universalUserToken1, "N/A", null);
    UsernamePasswordAuthenticationToken token2 = new UsernamePasswordAuthenticationToken(
        universalUserToken2, "N/A", null);

    authentication = new OAuth2Authentication(request, token);
    authentication1 = new OAuth2Authentication(request, token1);
    authentication2 = new OAuth2Authentication(request, token2);
  }

  @Before
  public void setUp() throws Exception {

    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.HOUR, 8);
    oAuth2AccessToken.setExpiration(cal.getTime());
    oAuth2AccessToken2.setExpiration(cal.getTime());
    oAuth2AccessTokenExpired.setExpiration(new Date());
  }

  @After
  public void tearDown() throws Exception {
    ((InMemoryTokenStore)tokenStore).clear();
    securityContext.setAuthentication(null);
  }

  @Test
  public void login() throws Exception {
    securityContext.setAuthentication(authentication);
    clientContext.setAccessToken(oAuth2AccessToken);
    String token = oauthLoginService.login("providerId");
    assertEquals(TOKEN, token);
  }

  @Test
  public void validate() throws Exception {
    //First Login
    securityContext.setAuthentication(authentication);
    clientContext.setAccessToken(oAuth2AccessToken);
    String token = oauthLoginService.login("providerId");
    assertEquals(TOKEN, token);

    String validateRes = oauthLoginService.validate(token);
    assertEquals(USER_ID, validateRes);
    assertEquals(((InMemoryTokenStore)tokenStore).getAccessTokenCount(), 1);

    // Second Login
    securityContext.setAuthentication(authentication1);
    clientContext.setAccessToken(oAuth2AccessTokenExpired);
    String tokenExpired = oauthLoginService.login("providerId");
    assertEquals(TOKEN_EXPIRED, tokenExpired);
    assertEquals(((InMemoryTokenStore)tokenStore).getAccessTokenCount(), 2);

    try {
      oauthLoginService.validate(tokenExpired);
      fail();
    } catch (IllegalArgumentException e) {
      // Do nothing expected exception
    }

    // Third login
    securityContext.setAuthentication(authentication2);
    clientContext.setAccessToken(oAuth2AccessToken2);
    String token2 = oauthLoginService.login("providerId");
    assertEquals(TOKEN2, token2);
    assertEquals(USER_ID2, oauthLoginService.validate(token2));
    //Expired token should be deleted
    assertEquals(((InMemoryTokenStore)tokenStore).getAccessTokenCount(), 2);
    
  }

}