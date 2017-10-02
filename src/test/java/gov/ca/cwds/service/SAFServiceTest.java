package gov.ca.cwds.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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

  @Before
  public void before() {
    safService = new SAFService();
    client = Mockito.mock(RestTemplate.class);
    safService.setClient(client);
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
    assert returnType.getValue()== Map.class;
    assert httpEntity.getValue().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).equals(HEADER);
  }

  @Test
  public void testInvalidate() throws SAFServiceException {
    ArgumentCaptor<String> uriArg = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<HttpEntity> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);
    ArgumentCaptor<Class> returnType = ArgumentCaptor.forClass(Class.class);
    safService.setRevokeTokenUri(URL);
    safService.invalidate(TOKEN);
    Mockito.verify(client, Mockito.times(1)).postForObject(uriArg.capture(),
            httpEntity.capture(),
            returnType.capture());
    assert uriArg.getValue().equals(URL);
    assert returnType.getValue()== String.class;
    assert httpEntity.getValue().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).equals(HEADER);
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
    assert returnType.getValue()== Map.class;
    assert httpEntity.getValue().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).equals(HEADER);
  }

  @Test(expected = SAFServiceException.class)
  public void testLogout() throws SAFServiceException{
    safService.logout();
  }
}