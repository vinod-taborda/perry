package gov.ca.cwds.rest;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleAccountLoginConfiguration {

  @JsonProperty
  @NotEmpty
  private String templateName;
  private String assetsPath;
  private String basePath;

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

  /**
   * @return the basePath
   */
  public String getBasePath() {
    return basePath;
  }


}
