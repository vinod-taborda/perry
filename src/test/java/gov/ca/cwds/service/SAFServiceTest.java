package gov.ca.cwds.service;

import gov.ca.cwds.config.OAuthConfiguration;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by dmitry.rudenko on 10/2/2017.
 */
public class SAFServiceTest {

  private SAFService safService;
  private ResourceServerProperties resourceServerProperties;
  private static final String URL = "URL";
  private static final String TOKEN = "token";
  private static final String HEADER = "bearer " + TOKEN;
  private RestTemplate client;
  private OAuth2RestTemplate oAuth2RestTemplate;

  @Before
  public void before() {
    safService = new SAFService();
    client = Mockito.mock(RestTemplate.class);
    oAuth2RestTemplate = Mockito.mock(OAuth2RestTemplate.class);
    safService.setClient(client);
    safService.setRestTemplate(oAuth2RestTemplate);
    resourceServerProperties = new ResourceServerProperties();
    resourceServerProperties.setTokenInfoUri(URL);
    resourceServerProperties.setUserInfoUri(URL);
    safService.setSso(resourceServerProperties);
  }

  @Test
  public void testValidate() throws SAFServiceException {
    ArgumentCaptor<String> uriArg = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<HttpEntity> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);
    ArgumentCaptor<Class> returnType = ArgumentCaptor.forClass(Class.class);
    safService.validate(TOKEN);
    Mockito.verify(client, Mockito.times(1)).postForObject(uriArg.capture(),
        httpEntity.capture(),
        returnType.capture());
    assert uriArg.getValue().equals(URL);
    assert returnType.getValue() == Map.class;
    assert httpEntity.getValue().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).equals(HEADER);
  }

  @Test
  public void testInvalidate() throws SAFServiceException {
    ArgumentCaptor<String> uriArg = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<HttpEntity> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);
    ArgumentCaptor<Class> returnType = ArgumentCaptor.forClass(Class.class);
    safService.setRevokeTokenUri(URL);
    OAuthConfiguration.ClientProperties clientProperties = new OAuthConfiguration.ClientProperties();
    clientProperties.setAccessTokenUri("setAccessTokenUri");

    HttpHeaders headers = new HttpHeaders();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("token", TOKEN);
    params.add("token_type_hint", "access_token");
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

    ResponseEntity response = Mockito.mock(ResponseEntity.class);
    Mockito.when(response.getBody()).thenReturn("OK");

    Mockito.when(oAuth2RestTemplate.postForEntity(URL, request, String.class))
        .thenReturn(response);

    safService.invalidate(TOKEN);
    Mockito.verify(oAuth2RestTemplate, Mockito.times(1)).postForEntity(
        uriArg.capture(),
        httpEntity.capture(),
        returnType.capture());
    assert uriArg.getValue().equals(URL);
    assert returnType.getValue() == String.class;
    assert httpEntity.getValue().getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)
        .equals("application/x-www-form-urlencoded");
  }

  @Test
  public void testGetUserInfo() throws SAFServiceException {
    ArgumentCaptor<String> uriArg = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<HttpEntity> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);
    ArgumentCaptor<Class> returnType = ArgumentCaptor.forClass(Class.class);
    safService.getUserInfo(TOKEN);
    Mockito.verify(client, Mockito.times(1)).postForObject(uriArg.capture(),
        httpEntity.capture(),
        returnType.capture());
    assert uriArg.getValue().equals(URL);
    assert returnType.getValue() == Map.class;
    assert httpEntity.getValue().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).equals(HEADER);
  }

  @Test(expected = SAFServiceException.class)
  public void testLogout() throws SAFServiceException {
    safService.logout();
  }
}