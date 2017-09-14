package gov.ca.cwds.security.shiro.realms;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.shiro.authc.AuthenticationException;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CWDS CALS API Team
 */
public class PerryRealm extends AbstractRealm {

  private static final Logger LOGGER = LoggerFactory.getLogger(PerryRealm.class);

  private String validationUri;

  private Client client;


  public String getValidationUri() {
    return validationUri;
  }

  public void setValidationUri(String validationUri) {
    this.validationUri = validationUri;
  }

  @Override
  protected String validate(String token) throws AuthenticationException {
    try {
      WebTarget target = client.target(validationUri).queryParam("token", token);
      Invocation.Builder invocation = target.request(MediaType.APPLICATION_JSON);
      Response response = invocation.get();
      int status = response.getStatus();
      if (status != 200) {
        throw new AuthenticationException("Failed : HTTP error code : "
            + status);
      }
      return response.readEntity(String.class);
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      throw new AuthenticationException(e);
    }
  }

  @Override
  protected void onInit() {
    super.onInit();
    JerseyClientBuilder clientBuilder = new JerseyClientBuilder()
        .property(ClientProperties.CONNECT_TIMEOUT, 5000)
        .property(ClientProperties.READ_TIMEOUT, 20000);
    client = clientBuilder.build();
  }
}
