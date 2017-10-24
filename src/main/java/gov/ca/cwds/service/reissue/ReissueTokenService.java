package gov.ca.cwds.service.reissue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by TPT2 on 10/24/2017.
 */
@Service
public class ReissueTokenService {
  @Autowired
  private ClientTokenServices clientTokenServices;
  @Autowired
  private OAuth2ProtectedResourceDetails resourceDetails;


  @PostConstruct
  public void test() {
    OAuth2AccessToken accessToken = new DefaultOAuth2AccessToken("test");
    Authentication authentication = new PreAuthenticatedAuthenticationToken("perry_token", "N/A");
    clientTokenServices.saveAccessToken(resourceDetails, authentication, accessToken);
    System.out.println(clientTokenServices);
  }
}
