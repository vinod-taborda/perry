package gov.ca.cwds.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * @author CWDS CALS API Team
 */
@Profile("prod")
@Component
public class OauthLogoutHandler implements LogoutHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(OauthLogoutHandler.class);

  private TokenService tokenService;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    try {
      OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
      tokenService.invalidate(oAuth2Authentication);
    } catch (Exception e) {
      LOGGER.error("Token invalidation error.",  e);
    }
  }

  @Autowired
  public void setTokenService(TokenService tokenService) {
    this.tokenService = tokenService;
  }
}
