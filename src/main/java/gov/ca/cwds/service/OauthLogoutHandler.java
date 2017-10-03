package gov.ca.cwds.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * @author CWDS CALS API Team
 */
@Profile("prod")
@Component
public class OauthLogoutHandler implements LogoutHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(OauthLogoutHandler.class);

  private TokenServiceImpl tokenService;
  @Autowired
  private TokenStore tokenStore;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    try {
      if (authentication == null) {
        LOGGER.warn("Authentication is NULL");
        return;
      }
      OAuth2AccessToken accessToken = tokenStore.getAccessToken((OAuth2Authentication)authentication);
      if (accessToken != null) {
        tokenService.invalidate(accessToken.getValue());
      }
    } catch (Exception e) {
      LOGGER.error("Token invalidation error.", e);
    }
  }

  @Autowired
  public void setTokenService(TokenService tokenService) {
    this.tokenService = tokenService;
  }
}
