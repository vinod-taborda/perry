package gov.ca.cwds;

import java.util.Set;

/**
 * Created by dmitry.rudenko on 7/28/2017.
 */
public class UniversalUserToken {
  private String userId;
  private Set<String> roles;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  public String toString() {
    return userId;
  }
}
