package gov.ca.cwds.rest.api.domain;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PerryUser {
  @NotNull
  private String username;

  @JsonCreator
  public PerryUser(@JsonProperty("username") String username) {
    super();
    this.username = username;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }



}
