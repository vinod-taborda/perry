package gov.ca.cwds.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author CWDS CALS API Team
 */
public interface LogoutService {
  void logout(HttpServletRequest request,
      HttpServletResponse response);
}
