package gov.ca.cwds.security.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author CWDS TPT-3 Team
 */
public class SecurityConfiguration {

  @JsonProperty
  private Boolean authorizationEnabled = Boolean.TRUE;

  public Boolean getAuthorizationEnabled() {
    return authorizationEnabled;
  }

  public void setAuthorizationEnabled(Boolean authorizationEnabled) {
    this.authorizationEnabled = authorizationEnabled;
  }

}
