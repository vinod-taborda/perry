package gov.ca.cwds.web.error;

import gov.ca.cwds.PerryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TPT2 on 11/20/2017.
 */
@Component
public class ErrorView {
  private PerryProperties properties;

  public PerryProperties getProperties() {
    return properties;
  }

  public boolean suppress(HttpServletRequest request) {
    if(properties.isSuppressErrorPage()) {
      String errorMessage = (String) request.getAttribute("javax.servlet.error.message");
      return errorMessage != null && errorMessage.contains("access token");
    }
    return false;
  }

  @Autowired
  public void setProperties(PerryProperties properties) {
    this.properties = properties;
  }
}
