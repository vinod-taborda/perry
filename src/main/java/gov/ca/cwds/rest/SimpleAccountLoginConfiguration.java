package gov.ca.cwds.rest;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleAccountLoginConfiguration {

  @JsonProperty
  @NotEmpty
  private String templateName;
  private String assetsPath;

  /**
   * @return the templateName
   */
  public String getTemplateName() {
    return templateName;
  }

  /**
   * @return the assetsPath
   */
  public String getAssetsPath() {
    return assetsPath;
  }
}
