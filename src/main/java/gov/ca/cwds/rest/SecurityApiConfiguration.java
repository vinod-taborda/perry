package gov.ca.cwds.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.client.JerseyClientConfiguration;

/**
 * 
 * 
 * @author CWDS API Team
 */
public class SecurityApiConfiguration extends BaseApiConfiguration {
  @NotNull
  private SAFConfiguration safConfiguration;

  @Valid
  @NotNull
  @JsonProperty("jerseyClient")
  private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

  /**
   * @return the safConfiguration
   */
  public SAFConfiguration getSafConfiguration() {
    return safConfiguration;
  }

  /**
   * @param safConfiguration the safConfiguration to set
   */
  @JsonProperty("saf")
  public void setSafConfiguration(SAFConfiguration safConfiguration) {
    this.safConfiguration = safConfiguration;
  }

  public JerseyClientConfiguration getJerseyClientConfiguration() {
    return jerseyClient;
  }

}
