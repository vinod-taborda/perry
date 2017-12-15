package gov.ca.cwds.rest.api;

import gov.ca.cwds.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by TPT2 on 11/21/2017.
 */
public class TokenResourceTest {
  public static final String TOKEN = "token";
  public static final String JSON = "JSON";
  private TokenResource loginResource;
  private LoginService loginService;
  private HttpServletResponse response;

  @Before
  public void before() {
    loginResource = new TokenResource();
    loginService = Mockito.mock(LoginService.class);
    loginResource.setLoginService(loginService);
    response = Mockito.mock(HttpServletResponse.class);
  }

  @Test
  public void testValidateToken() throws Exception {
    Mockito.when(loginService.validate(TOKEN)).thenReturn(JSON);
    String json = loginResource.validateToken(response, TOKEN);
    assert json.equals(JSON);
  }

  @Test(expected = Exception.class)
  public void testValidateFailedToken() throws Exception {
    Mockito.when(loginService.validate(TOKEN)).thenThrow(new Exception());
    String json = loginResource.validateToken(response, TOKEN);
  }

  @Test
  public void testInvalidateToken() throws Exception {
    Mockito.when(loginService.validate(TOKEN)).thenThrow(new Exception());
    String json = loginResource.invalidate(response, TOKEN);
    Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    assert json.equals("OK");
  }
}
