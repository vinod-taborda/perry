package gov.ca.cwds.service.reissue;

import gov.ca.cwds.rest.api.domain.PerryException;
import gov.ca.cwds.service.SAFServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static gov.ca.cwds.config.Constants.IDENTITY;

/**
 * Created by TPT2 on 10/24/2017.
 */
@Service
public class ReissueTokenService {
  @Value("${security.oauth2.resource.revokeTokenUri}")
  private String revokeTokenUri;

  private ClientTokenServices clientTokenServices;
  private OAuth2ProtectedResourceDetails resourceDetails;
  private ResourceServerProperties resourceServerProperties;

  public String storeAccessToken(OAuth2AccessToken accessToken) {
    return storeAccessToken(accessToken, generatePerryToken());
  }

  public String validate(String perryToken) {
    OAuth2AccessToken accessToken = getAccessToken(perryToken);
    OAuth2RestTemplate restTemplate = createRestTemplate(accessToken);
    restTemplate.postForObject(resourceServerProperties.getTokenInfoUri(), null, String.class);
    String identity = (String) accessToken.getAdditionalInformation().get(IDENTITY);
    OAuth2AccessToken reissuedAccessToken = restTemplate.getOAuth2ClientContext().getAccessToken();
    if (reissuedAccessToken != accessToken) {
      reissuedAccessToken.getAdditionalInformation().put(IDENTITY, identity);
      storeAccessToken(reissuedAccessToken, perryToken);
    }
    return identity;
  }

  public void invalidate(String perryToken) {
    OAuth2AccessToken accessToken = removeAccessToken(perryToken);
    OAuth2RestTemplate restTemplate = createRestTemplate(null);
    try {
      HttpHeaders headers = new HttpHeaders();
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("token", accessToken.getValue());
      params.add("token_type_hint", "access_token");
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
      restTemplate.postForEntity(revokeTokenUri, request, String.class).getBody();
    } catch (Exception e) {
      throw new PerryException(
              "Token Revocation problem for revokeTokenUri = " + revokeTokenUri, e);
    }
  }

  public OAuth2AccessToken removeAccessToken(String perryToken) {
    Authentication authentication = toAuthentication(perryToken);
    OAuth2AccessToken accessToken = getAccessToken(authentication);
    clientTokenServices.removeAccessToken(resourceDetails, authentication);
    return accessToken;
  }

  private OAuth2RestTemplate createRestTemplate(OAuth2AccessToken accessToken) {
    OAuth2ClientContext clientContext = new DefaultOAuth2ClientContext(accessToken);
    return new OAuth2RestTemplate(resourceDetails, clientContext);
  }

  private String generatePerryToken() {
    return UUID.randomUUID().toString();
  }

  private Authentication toAuthentication(String perryToken) {
    return new PreAuthenticatedAuthenticationToken(perryToken, "N/A");
  }

  public OAuth2AccessToken getAccessToken(String perryToken) {
    return getAccessToken(toAuthentication(perryToken));
  }

  private OAuth2AccessToken getAccessToken(Authentication authentication) {
    OAuth2AccessToken accessToken = clientTokenServices.getAccessToken(resourceDetails, authentication);
    if (accessToken == null) {
      throw new PerryException("invalid request");
    }
    return accessToken;
  }

  public String storeAccessToken(OAuth2AccessToken accessToken, String perryToken) {
    Authentication authentication = toAuthentication(perryToken);
    clientTokenServices.saveAccessToken(resourceDetails, authentication, accessToken);
    return perryToken;
  }

  @Autowired
  public void setClientTokenServices(ClientTokenServices clientTokenServices) {
    this.clientTokenServices = clientTokenServices;
  }

  @Autowired
  public void setResourceDetails(OAuth2ProtectedResourceDetails resourceDetails) {
    this.resourceDetails = resourceDetails;
  }

  @Autowired
  public void setResourceServerProperties(ResourceServerProperties resourceServerProperties) {
    this.resourceServerProperties = resourceServerProperties;
  }

}
