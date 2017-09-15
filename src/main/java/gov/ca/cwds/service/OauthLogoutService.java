package gov.ca.cwds.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

/**
 * @author CWDS CALS API Team
 */
@Profile("prod")
@Service
public class OauthLogoutService implements LogoutService {

  private static final Logger LOGGER = LoggerFactory.getLogger(OauthLogoutService.class);
    
  @Autowired
  private TokenService tokenService;

  @Autowired
  private SAFService safService;

  @Autowired
  private SecurityContextLogoutHandler logoutHandler;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {

    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication authentication = securityContext.getAuthentication();
    if (authentication != null) {
      tokenService.invalidate((OAuth2Authentication) authentication);
      logoutHandler.logout(request, response, authentication);
    }
    try {
      safService.logout();
    } catch (SAFServiceException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }
}
