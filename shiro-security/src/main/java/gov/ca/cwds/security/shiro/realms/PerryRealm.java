package gov.ca.cwds.security.shiro.realms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CWDS CALS API Team
 */
public class PerryRealm extends AbstractRealm {

  private static final Logger LOGGER = LoggerFactory.getLogger(PerryRealm.class);

  public String getValidationUrl() {
    return validationUrl;
  }

  public void setValidationUrl(String validationUrl) {
    this.validationUrl = validationUrl;
  }

  private String validationUrl;

  @Override
  protected String validate(String token) throws AuthenticationException {
    try {
      URL url = new URL(validationUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");
      if (conn.getResponseCode() != 200) {
        throw new AuthenticationException("Failed : HTTP error code : "
            + conn.getResponseCode());
      }
      BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
      conn.disconnect();
      return sb.toString();
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      throw new AuthenticationException(e);
    }
  }
}
