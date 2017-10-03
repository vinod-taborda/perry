package gov.ca.cwds.rest.api;

import gov.ca.cwds.service.TokenLoginService;
import gov.ca.cwds.service.TokenService;
import gov.ca.cwds.service.WhiteList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dmitry.rudenko on 10/3/2017.
 */
public class LoginResourceV2Test {
  public static final String SP_ID = "sp_id";
  public static final String TOKEN = "token";
  public static final String CALLBACK = "callback";
  public static final String JSON = "JSON";
  private LoginResourceV2 loginResource;
  private TokenLoginService loginService;
  private TokenService tokenService;
  private WhiteList whiteList;
  private HttpServletResponse response;
  private HttpServletRequest request;

  @Before
  public void before() {
    loginResource = new LoginResourceV2();
    loginService = Mockito.mock(TokenLoginService.class);
    tokenService = Mockito.mock(TokenService.class);
    whiteList = Mockito.mock(WhiteList.class);
    loginResource.setLoginService(loginService);
    loginResource.setWhiteList(whiteList);
    loginResource.setTokenService(tokenService);
    response = Mockito.mock(HttpServletResponse.class);
    request = Mockito.mock(HttpServletRequest.class);
  }

  @Test
  public void testLogin() throws Exception {
    Mockito.when(loginService.login(SP_ID)).thenReturn(TOKEN);
    loginResource.loginV2(response, CALLBACK, SP_ID);
    Mockito.verify(response).sendRedirect(CALLBACK + "?token=" + TOKEN);
  }

  @Test
  public void testValidateToken() throws Exception {
    Mockito.when(loginService.validate(TOKEN)).thenReturn(JSON);
    String json = loginResource.validateTokenV2(response, TOKEN);
    assert json.equals(JSON);
  }

  @Test
  public void testValidateFailedToken() throws Exception {
    Mockito.when(loginService.validate(TOKEN)).thenThrow(new Exception());
    String json = loginResource.validateTokenV2(response, TOKEN);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    assert json.equals("Unauthorized");
  }

  @Test
  public void testInvalidateToken() throws Exception {
    String json = loginResource.invalidate(response, TOKEN);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    assert json.equals("OK");
  }

}
