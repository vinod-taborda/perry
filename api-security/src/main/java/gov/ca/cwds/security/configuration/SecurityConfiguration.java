package gov.ca.cwds.security.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author CWDS TPT-3 Team
 */
public class SecurityConfiguration {

  @JsonProperty
  private Boolean authorization = Boolean.TRUE;

  public Boolean getAuthorization() {
    return authorization;
  }

  public void setAuthorization(Boolean authorization) {
    this.authorization = authorization;
  }

}
