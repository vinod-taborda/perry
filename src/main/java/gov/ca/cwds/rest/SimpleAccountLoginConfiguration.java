package gov.ca.cwds.rest;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleAccountLoginConfiguration {

  @JsonProperty
  @NotEmpty
  private String templateName;

  /**
   * @return the templateName
   */
  public String getTemplateName() {
    return templateName;
  }
}
