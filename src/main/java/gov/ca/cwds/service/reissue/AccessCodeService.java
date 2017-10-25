package gov.ca.cwds.service.reissue;

import gov.ca.cwds.UniversalUserToken;
import gov.ca.cwds.config.Constants;
import gov.ca.cwds.service.IdentityMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

/**
 * Created by TPT2 on 10/24/2017.
 */
@Service
public class AccessCodeService {

  private ReissueTokenService reissueTokenService;

  private IdentityMappingService identityMappingService;

  public String issueAccessCode(String providerId, OAuth2ClientContext oauth2ClientContext) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    OAuth2Authentication authentication = (OAuth2Authentication) securityContext.getAuthentication();
    UniversalUserToken userToken = (UniversalUserToken) authentication.getPrincipal();
    OAuth2AccessToken accessToken = oauth2ClientContext.getAccessToken();
    String identity = identityMappingService.map(userToken, providerId);
    accessToken.getAdditionalInformation().put(Constants.IDENTITY, identity);
    return reissueTokenService.storeAccessToken(accessToken);
  }

  public String issueToken(String accessCode) {
    OAuth2AccessToken accessToken = reissueTokenService.removeAccessToken(accessCode);
    return reissueTokenService.storeAccessToken(accessToken);
  }

  @Autowired
  public void setIdentityMappingService(IdentityMappingService identityMappingService) {
    this.identityMappingService = identityMappingService;
  }

  @Autowired
  public void setReissueTokenService(ReissueTokenService reissueTokenService) {
    this.reissueTokenService = reissueTokenService;
  }
}
