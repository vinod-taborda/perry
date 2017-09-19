package gov.ca.cwds.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.ca.cwds.PerryProperties;
import gov.ca.cwds.service.WhiteList;
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
  @Autowired
  WhiteList whiteList;

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    String callback = request.getParameter("callback");
    if (callback != null) {
      whiteList.validate("callback", callback);
      response.sendRedirect(callback);
    } else {
      super.onLogoutSuccess(request, response, authentication);
    }
  }
}
