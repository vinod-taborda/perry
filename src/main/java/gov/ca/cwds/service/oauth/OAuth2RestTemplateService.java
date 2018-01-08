package gov.ca.cwds.service.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by TPT2 on 10/27/2017.
 */
@Service
@Profile("prod")
public class OAuth2RestTemplateService {
  private OAuth2ProtectedResourceDetails resourceDetails;
  private OAuth2RestTemplate clientTemplate;
  private ResourceServerProperties resourceServerProperties;

  @PostConstruct
  public void init() {
    ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
    resource.setAccessTokenUri(resourceDetails.getAccessTokenUri());
    resource.setClientId(resourceServerProperties.getClientId());
    resource.setClientSecret(resourceServerProperties.getClientSecret());
    resource.setAuthenticationScheme(resourceDetails.getAuthenticationScheme());
    resource.setClientAuthenticationScheme(resourceDetails.getClientAuthenticationScheme());
    clientTemplate = new OAuth2RestTemplate(resource);
  }

  public OAuth2RestTemplate restTemplate(OAuth2AccessToken accessToken) {
    return new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext(accessToken));
  }

  public OAuth2RestTemplate restTemplate(String accessToken) {
    return new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext(new DefaultOAuth2AccessToken(accessToken)));
  }

  public OAuth2RestTemplate clientRestTemplate() {
    return clientTemplate;
  }

  @Autowired
  public void setResourceServerProperties(ResourceServerProperties resourceServerProperties) {
    this.resourceServerProperties = resourceServerProperties;
  }

  @Autowired
  public void setResourceDetails(OAuth2ProtectedResourceDetails resourceDetails) {
    this.resourceDetails = resourceDetails;
  }
}
