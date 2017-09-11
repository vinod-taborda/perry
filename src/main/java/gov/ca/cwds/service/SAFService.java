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
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    String bearer = BEARER + accessToken;
    headers.add(HttpHeaders.AUTHORIZATION, bearer);
    return client.postForObject(sso.getUserInfoUri(), new HttpEntity<>(bearer, headers), Map.class);
  }

  public String validate(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    String bearer = BEARER + token;
    headers.add(HttpHeaders.AUTHORIZATION, bearer);
    return client.postForObject(sso.getTokenInfoUri(), new HttpEntity<>(bearer, headers), String.class);
  }
}
