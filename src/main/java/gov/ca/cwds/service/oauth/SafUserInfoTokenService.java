package gov.ca.cwds.service.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.List;
import java.util.Map;

/**
 * Created by dmitry.rudenko on 5/30/2017.
 */
public class SafUserInfoTokenService extends UserInfoTokenServices {

  private PrincipalExtractor principalExtractor;
  private OAuth2RestTemplateService restClientService;
  private ResourceServerProperties resourceServerProperties;

  public SafUserInfoTokenService(ResourceServerProperties resourceServerProperties) {
    super(resourceServerProperties.getUserInfoUri(), resourceServerProperties.getClientId());
    this.resourceServerProperties = resourceServerProperties;
  }

  @Override
  @SuppressWarnings("unchecked")
  public OAuth2Authentication loadAuthentication(String accessToken) {
    Map userInfo = restClientService.restTemplate(accessToken).postForObject(resourceServerProperties.getUserInfoUri(), null, Map.class);
    Object principal = principalExtractor.extractPrincipal(userInfo);
    OAuth2Request request = new OAuth2Request(null, resourceServerProperties.getClientId(), null, true, null,
            null, null, null, null);
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            principal, "N/A", extractAuthorities(userInfo));
    token.setDetails(userInfo);
    return new OAuth2Authentication(request, token);
  }

  private List<GrantedAuthority> extractAuthorities(Map map) {
    return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
  }

  @Override
  @Autowired
  public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
    this.principalExtractor = principalExtractor;
  }

  @Autowired
  public void setRestClientService(OAuth2RestTemplateService restClientService) {
    this.restClientService = restClientService;
  }
}
