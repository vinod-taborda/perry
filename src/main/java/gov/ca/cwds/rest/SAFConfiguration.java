package gov.ca.cwds.rest;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

/**
 * Configuration class containing information needed to interact with SAF
 * 
 * @author CWDS API Team
 */
public class SAFConfiguration extends Configuration {

  @JsonProperty
  @NotEmpty
  private String baseUrl;

  @JsonProperty
  @NotEmpty
  private String authPath;

  @JsonProperty
  @NotEmpty
  private String retrieveTokenPath;

  @JsonProperty
  @NotEmpty
  private String validateTokenPath;

  @JsonProperty
  @NotEmpty
  private String clientId;

  @JsonProperty
  @NotEmpty
  private String clientSecret;

  @JsonProperty
  @NotEmpty
  private String callbackUrl;

  @JsonProperty
  @NotEmpty
  private String scope;

  /**
   * @return the baseUrl
   */
  public String getBaseUrl() {
    return baseUrl;
  }

  /**
   * @return the authPath
   */
  public String getAuthPath() {
    return authPath;
  }

  /**
   * @return the retrieveTokenPath
   */
  public String getRetrieveTokenPath() {
    return retrieveTokenPath;
  }

  /**
   * @return the validateTokenPath
   */
  public String getValidateTokenPath() {
    return validateTokenPath;
  }

  /**
   * @return the clientId
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * @return the clientSecret
   */
  public String getClientSecret() {
    return clientSecret;
  }

  /**
   * @return the callbackUrl
   */
  public String getCallbackUrl() {
    return callbackUrl;
  }

  /**
   * @return the scope
   */
  public String getScope() {
    return scope;
  }
}
