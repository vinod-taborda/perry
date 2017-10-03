package gov.ca.cwds.rest.api;

import gov.ca.cwds.service.LoginService;
import gov.ca.cwds.service.WhiteList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dmitry.rudenko on 10/3/2017.
 */
public class LoginResourceTest {
  public static final String SP_ID = "sp_id";
  public static final String TOKEN = "token";
  public static final String CALLBACK = "callback";
  public static final String JSON = "JSON";
  private LoginResource loginResource;
  private LoginService loginService;
  private WhiteList whiteList;
  private HttpServletResponse response;
  private HttpServletRequest request;

  @Before
  public void before() {
    loginResource = new LoginResource();
    loginService = Mockito.mock(LoginService.class);
    whiteList = Mockito.mock(WhiteList.class);
    loginResource.setLoginService(loginService);
    loginResource.setWhiteList(whiteList);
    response = Mockito.mock(HttpServletResponse.class);
    request = Mockito.mock(HttpServletRequest.class);
  }

  @Test
  public void testLogin() throws Exception {
    Mockito.when(loginService.login(SP_ID)).thenReturn(TOKEN);
    loginResource.login(response, CALLBACK, SP_ID);
    Mockito.verify(response).sendRedirect(CALLBACK + "?token=" + TOKEN);
  }

  @Test
  public void testValidateToken() throws Exception {
    Mockito.when(loginService.validate(TOKEN)).thenReturn(JSON);
    String json = loginResource.validateToken(response, TOKEN);
    assert json.equals(JSON);
  }

  @Test
  public void testValidateFailedToken() throws Exception {
    Mockito.when(loginService.validate(TOKEN)).thenThrow(new Exception());
    String json = loginResource.validateToken(response, TOKEN);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    assert json.equals("Unauthorized");
  }

  @Test
  public void testInvalidateToken() throws Exception {
    Mockito.when(loginService.validate(TOKEN)).thenThrow(new Exception());
    String json = loginResource.invalidate(response, TOKEN);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    assert json.equals("OK");
  }
}
