package gov.ca.cwds.service;

import java.util.Map;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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
  @Autowired
  private RestTemplate client;
  @JsonIgnore
  @Autowired
  private ResourceServerProperties sso;

  private String revokeTokenUri;

  public String getRevokeTokenUri() {
    return revokeTokenUri;
  }

  public void setRevokeTokenUri(String revokeTokenUri) {
    this.revokeTokenUri = revokeTokenUri;
  }

  public Map getUserInfo(String accessToken) {
    return callSaf(sso.getUserInfoUri(), accessToken, Map.class);
  }

  public Map validate(String token) {
    return callSaf(sso.getTokenInfoUri(), token, Map.class);
  }

  public void invalidate(String token) {
    callSaf(revokeTokenUri, token, String.class);

  }

  protected <T> T callSaf(String uri, String token, Class<T> returnType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    String bearer = BEARER + token;
    headers.add(HttpHeaders.AUTHORIZATION, bearer);
    return client.postForObject(uri, new HttpEntity<>(bearer, headers), returnType);
  }
}
