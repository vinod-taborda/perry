package gov.ca.cwds.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;

import gov.ca.cwds.config.OAuthConfiguration;
import gov.ca.cwds.config.OAuthConfiguration.ClientProperties;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * Created by dmitry.rudenko on 10/2/2017.
 */
public class SAFServiceTest {
  private SAFService safService;
  private ResourceServerProperties resourceServerProperties;
  private ClientProperties clientProperties;
  private static final String URL = "URL";
  private static final String TOKEN = "token";
  private static final String CLIENT_TOKEN = "client_token";
  private static final String HEADER = "bearer " + TOKEN;
  private static final String CLIENT_TOKEN_HEADER = "bearer " + CLIENT_TOKEN;
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
    clientProperties = new ClientProperties();
    clientProperties.setAccessTokenUri(URL);
    safService.setClientProperties(clientProperties);
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
    Mockito.when(client.getForObject(anyString(), anyObject())).thenReturn(CLIENT_TOKEN);
    ArgumentCaptor<String> uriArg = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<HttpEntity> httpEntity = ArgumentCaptor.forClass(HttpEntity.class);
    ArgumentCaptor<Class> returnType = ArgumentCaptor.forClass(Class.class);
    safService.setRevokeTokenUri(URL);
    OAuthConfiguration.ClientProperties clientProperties = new OAuthConfiguration.ClientProperties();
    clientProperties.setAccessTokenUri("setAccessTokenUri");
    safService.setClientProperties(clientProperties);


    Mockito.when(client.getForObject(
            "setAccessTokenUri?client_id=null&client_secret=null&grant_type=client_credentials",
            String.class))
            .thenReturn(TOKEN);


    safService.invalidate(TOKEN);
    Mockito.verify(client, Mockito.times(1)).postForObject(uriArg.capture(),
            httpEntity.capture(),
            returnType.capture());
    assert uriArg.getValue().equals(URL + "?token="+ TOKEN +"&token_type_hint=access_token");
    assert returnType.getValue()== String.class;
    assert httpEntity.getValue().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0).equals(CLIENT_TOKEN_HEADER);
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
