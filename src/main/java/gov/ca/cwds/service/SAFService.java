package gov.ca.cwds.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
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
public class SAFService {

  private static final String BEARER = "bearer ";
  @Autowired
  private RestTemplate client;
  @Autowired
  private ResourceServerProperties sso;

  public Map getUserInfo(String accessToken) {
    return callSaf(sso.getUserInfoUri(), accessToken, Map.class);
  }

  public String validate(String token) {
    return callSaf(sso.getTokenInfoUri(), token, String.class);
  }

  protected <T> T callSaf(String uri, String token, Class<T> returnType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    String bearer = BEARER + token;
    headers.add(HttpHeaders.AUTHORIZATION, bearer);
    return client.postForObject(uri, new HttpEntity<>(bearer, headers), returnType);
  }
}
