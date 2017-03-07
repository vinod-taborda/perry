package gov.ca.cwds.rest.resources.auth;

import static org.mockito.Mockito.mock;

import org.hamcrest.junit.ExpectedException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import io.dropwizard.testing.junit.ResourceTestRule;


public class LoginResourceTest {

  private static final String LOGINGET_RESOURCE = "/authn/login?callback=dlkjkdfd";
  private static final String PARAM_CALLBACK = "callback";

  @SuppressWarnings("javadoc")
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private final static LoginResourceHelper loginResourceHelper = mock(LoginResourceHelper.class);

  @SuppressWarnings("javadoc")
  @ClassRule
  public final static ResourceTestRule inMemoryResource =
      ResourceTestRule.builder().addResource(new LoginResource(loginResourceHelper)).build();

  @SuppressWarnings("javadoc")
  @Before
  public void setup() throws Exception {
    Mockito.reset(loginResourceHelper);
  }

  @Test
  public void loginGetDelegatesToResourceHelper() throws Exception {
    // Response response = inMemoryResource.client().target(LOGINGET_RESOURCE).request()
    // .accept(MediaType.TEXT_PLAIN).get();
    //
    // verify(loginResourceHelper).loginPage("some_url");
  }

}
