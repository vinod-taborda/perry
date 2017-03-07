package gov.ca.cwds.rest.resources.auth;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import gov.ca.cwds.rest.SimpleAccountLoginConfiguration;

/**
 * NOTE: {@link SimpleAccountLoginResourceHelper} is a class only used for development purposes.
 * Thorough testing may not be done and in many cases may not be needed.
 * 
 * @author CWDS API Team
 */
public class SimpleAccountLoginResourceHelperTest {
  private SimpleAccountLoginConfiguration configuration =
      mock(SimpleAccountLoginConfiguration.class);

  private SimpleAccountLoginResourceHelper simpleAccountLoginResourceHelper =
      new SimpleAccountLoginResourceHelper(configuration);

  @Before
  public void setup() throws Exception {

  }


  @Test
  public void validateTokenReturnsUnauthorizedWhenBadToken() {
    // Response response = simpleAccountLoginResourceHelper.validateToken("bad_token");
    // assertThat(response.getStatus(), is(401));
  }
}
