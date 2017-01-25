package gov.ca.cwds.rest.resources.auth.saf;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import gov.ca.cwds.rest.SAFConfiguration;
import gov.ca.cwds.rest.SecurityApiConfiguration;
import gov.ca.cwds.rest.resources.auth.LoginResource;
import io.dropwizard.testing.junit.ResourceTestRule;

public class OauthLoginResourceTest {

  private static final String LOGIN_RESOURCE = "/authn/login";
  private static final String NO_LOGIN_RESOURCE = "/authn/logins";
  private static final Client client;
  private static final File file;
  private static final ObjectMapper objectMapper;


  private static SAFConfiguration config = mock(SAFConfiguration.class);
  private static SecurityApiConfiguration securityconfig;

  private static String baseUrl = "https://sectest.dss.ca.gov/web1/dss_saf";
  private static String authPath = "/auth/v2/oauth2/authorize";
  private static String validateTokenPath = "/data/v2/api/client/41/auth/validatetoken";
  private static String clientId = "ec591707-be4e-4de5-8fc7-7cff83b04f65";
  private static String clientSecret = "5Gp7dQ6Fh7BMs0HlkiQ99";
  private static String callbackUrl = "http://54.70.247.41/cwds/callback";
  private static String scope = "basic_profile";

  // TODO : More Clean up is required

  static {
    client = mock(Client.class);
    file = new File("config/perry.yml");
    objectMapper = new ObjectMapper(new YAMLFactory());

    when(config.getBaseUrl()).thenReturn(baseUrl);
    when(config.getAuthPath()).thenReturn(authPath);
    when(config.getClientId()).thenReturn(clientId);
    when(config.getCallbackUrl()).thenReturn(callbackUrl);
  }

  @Before
  public void setup() {
    when(config.getBaseUrl()).thenReturn(baseUrl);
    when(config.getAuthPath()).thenReturn(authPath);
    when(config.getClientId()).thenReturn(clientId);
    when(config.getCallbackUrl()).thenReturn(callbackUrl);
    // config = securityconfig.getSafConfiguration();
  }

  private static OauthLoginResource targetHelper = new OauthLoginResource(config, client);
  private static LoginResource target = new LoginResource(targetHelper);
  @ClassRule
  public static ResourceTestRule RULE = ResourceTestRule.builder()
      .setTestContainerFactory(new GrizzlyWebTestContainerFactory()).addResource(target).build();


  @Test
  public void testLoginForSuccessRedirect() throws Exception {
    final Response response = RULE.getJerseyTest().target(LOGIN_RESOURCE)
        .queryParam("callback", "/foo").request(MediaType.APPLICATION_JSON).get();

    final Response expected = Response.status(Response.Status.FOUND).entity(null).build();
    assertThat(response.getStatus(), is(expected.getStatus()));
  }

  @Test
  public void testLoginForInvalidResource() throws Exception {
    final Response response =
        RULE.getJerseyTest().target(NO_LOGIN_RESOURCE).request(MediaType.APPLICATION_JSON).get();
    final Response expected = Response.status(Response.Status.NOT_FOUND).entity(null).build();
    assertThat(response.getStatus(), is(expected.getStatus()));
  }

  @Test
  public void testLoginForInvalidMethod() throws Exception {
    final Response response = RULE.getJerseyTest().target(LOGIN_RESOURCE).request().delete();
    final Response expected =
        Response.status(Response.Status.METHOD_NOT_ALLOWED).entity(null).build();
    assertThat(response.getStatus(), is(expected.getStatus()));
  }

  @Test
  public void testLoginWithInvalidUri() throws Exception {
    final Response response =
        RULE.getJerseyTest().target(NO_LOGIN_RESOURCE).request(MediaType.APPLICATION_JSON).get();
    final Response expected = Response.status(Response.Status.NOT_FOUND).entity(null).build();
    assertThat(response.getStatus(), is(expected.getStatus()));
  }

}
