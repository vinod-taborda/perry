package gov.ca.cwds.web;

import gov.ca.cwds.service.WhiteList;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author CWDS CALS API Team
 */
@Component
public class PerryLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements
    LogoutSuccessHandler {

  @JsonIgnore
  @Autowired
  protected WhiteList whiteList;

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    String callback = request.getParameter("callback");
    if (!tryRedirect(response, callback)) {
      super.onLogoutSuccess(request, response, authentication);
    }
  }

  protected boolean tryRedirect(HttpServletResponse response, String callback) throws IOException {
    boolean result;
    if (callback != null) {
      whiteList.validate("callback", callback);
      response.sendRedirect(callback);
      result = Boolean.TRUE;
    } else {
      result = Boolean.FALSE;
    }
    return result;
  }

}
