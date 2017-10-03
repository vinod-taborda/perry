package gov.ca.cwds.service;

import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@Service
@Profile("prod")
@ConfigurationProperties(prefix = "security.oauth2.resource")
public class SAFService {

  private static final String BEARER = "bearer ";
  @JsonIgnore
  private RestTemplate client;

  @JsonIgnore
  private OAuth2RestTemplate restTemplate;

  @JsonIgnore
  private ResourceServerProperties sso;

  private String revokeTokenUri;

  public String getRevokeTokenUri() {
    return revokeTokenUri;
  }

  public void setRevokeTokenUri(String revokeTokenUri) {
    this.revokeTokenUri = revokeTokenUri;
  }

  public Map getUserInfo(String accessToken) throws SAFServiceException {
    try {
      return callSaf(sso.getUserInfoUri(), accessToken, Map.class);
    } catch (Exception e) {
      throw new SAFServiceException(e);
    }

  }

  @Autowired
  public void setClient(RestTemplate client) {
    this.client = client;
  }

  @Autowired
  public void setSso(ResourceServerProperties sso) {
    this.sso = sso;
  }

  @Autowired
  public void setRestTemplate(OAuth2RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Map validate(String token) throws SAFServiceException {
    try {
      return callSaf(sso.getTokenInfoUri(), token, Map.class);
    } catch (Exception e) {
      throw new SAFServiceException(e);
    }
  }

  public String invalidate(String token) throws SAFServiceException {
    try {
      HttpHeaders headers = new HttpHeaders();
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("token", token);
      params.add("token_type_hint", "access_token");
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
      return restTemplate.postForEntity(revokeTokenUri, request, String.class).getBody();
    } catch (Exception e) {
      throw new SAFServiceException(
          "Token Revocation problem for revokeTokenUri = " + revokeTokenUri, e);
    }
  }

  public String logout() throws SAFServiceException {
    throw new SAFServiceException(new NotImplementedException("The method is not implemented yet"));
  }

  protected <T> T callSaf(String uri, String token, Class<T> returnType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    String bearer = BEARER + token;
    headers.add(HttpHeaders.AUTHORIZATION, bearer);
    return client.postForObject(uri, new HttpEntity<>(bearer, headers), returnType);
  }


}
