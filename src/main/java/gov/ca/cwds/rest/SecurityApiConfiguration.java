package gov.ca.cwds.rest;

import javax.annotation.Nullable;
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
  @Nullable
  private SAFConfiguration safConfiguration;

  @Nullable
  SimpleAccountLoginConfiguration simpleAccountLoginConfiguration;

  @Valid
  @NotNull
  @JsonProperty("jerseyClient")
  private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

  @NotNull
  private String loginResourceHelper;

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

  /**
   * @param simpleAccountLoginConfiguration the simpleAccountLoginConfiguration to set
   */
  @JsonProperty("simpleAccountLogin")
  public void setSimpleAccountLoginConfiguration(
      SimpleAccountLoginConfiguration simpleAccountLoginConfiguration) {
    this.simpleAccountLoginConfiguration = simpleAccountLoginConfiguration;
  }

  /**
   * @return the simpleAccountLoginConfiguration
   */
  public SimpleAccountLoginConfiguration getSimpleAccountLoginConfiguration() {
    return simpleAccountLoginConfiguration;
  }

  /**
   * @return the loginResourceHelper
   */
  public String getLoginResourceHelper() {
    return loginResourceHelper;
  }

  /**
   * @param loginResourceHelper the loginResourceHelper to set
   */
  @JsonProperty("loginResourceHelper")
  public void setLoginResourceHelper(String loginResourceHelper) {
    this.loginResourceHelper = loginResourceHelper;
  }


}
