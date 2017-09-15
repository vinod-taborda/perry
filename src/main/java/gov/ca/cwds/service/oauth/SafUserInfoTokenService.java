package gov.ca.cwds.service.oauth;

import gov.ca.cwds.service.SAFService;
import gov.ca.cwds.service.SAFServiceException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

/**
 * Created by dmitry.rudenko on 5/30/2017.
 */
public class SafUserInfoTokenService extends UserInfoTokenServices {

  private SAFService safService;
  private String clientId;
  @Autowired
  private PrincipalExtractor principalExtractor;

  public SafUserInfoTokenService(SAFService safService, String userInfoEndpointUrl,
      String clientId) {
    super(userInfoEndpointUrl, clientId);
    this.clientId = clientId;
    this.safService = safService;
  }

  @Override
  @SuppressWarnings("unchecked")
  public OAuth2Authentication loadAuthentication(String accessToken) {
    Map userInfo = null;

    try {
      userInfo = safService.getUserInfo(accessToken);
    } catch (SAFServiceException e) {
      logger.error("Problems during getting UserInfo from SAF", e);
      throw new IllegalStateException(e);
    }
    
    Object principal = principalExtractor.extractPrincipal(userInfo);
    OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null,
        null, null, null, null);
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        principal, "N/A", extractAuthorities(userInfo));
    token.setDetails(userInfo);
    return new OAuth2Authentication(request, token);
  }

  private List<GrantedAuthority> extractAuthorities(Map map) {
    return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
  }

}
