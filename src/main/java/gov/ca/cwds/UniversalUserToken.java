package gov.ca.cwds;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by dmitry.rudenko on 7/28/2017.
 */
public class UniversalUserToken {
  private String userId;
  private Set<String> roles;
  private Map<String, Object> parameters = new HashMap<>();

  public Object getParameter(String parameterName) {
    return parameters.get(parameterName);
  }

  public Object setParameter(String parameterName, Object parameter) {
    return parameters.put(parameterName, parameter);
  }

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
