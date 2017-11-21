package gov.ca.cwds.web.error;

import gov.ca.cwds.PerryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by TPT2 on 11/20/2017.
 */
@Component
public class ErrorView {
  private PerryProperties properties;

  public PerryProperties getProperties() {
    return properties;
  }


  @Autowired
  public void setProperties(PerryProperties properties) {
    this.properties = properties;
  }
}
