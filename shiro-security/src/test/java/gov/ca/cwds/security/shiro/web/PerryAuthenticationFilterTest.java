package gov.ca.cwds.security.shiro.web;

import gov.ca.cwds.security.shiro.PerryShiroToken;
import static org.junit.Assert.*;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

/**
 * Created by dmitry.rudenko on 7/25/2017.
 */
public class PerryAuthenticationFilterTest {

  private static final String ACCESS_TOKEN = "access_token";

  @Test
  public void testCreateToken() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    when(request.getParameter("token")).thenReturn(ACCESS_TOKEN);
    PerryAuthenticatingFilter perryAuthenticatingFilter = new PerryAuthenticatingFilter();
    PerryShiroToken authenticationToken = (PerryShiroToken) perryAuthenticatingFilter.createToken(request, response);
    assertEquals(ACCESS_TOKEN, authenticationToken.getToken());
  }

}
