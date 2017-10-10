package gov.ca.cwds.security;

import org.apache.shiro.authc.AuthenticationToken;


@SuppressWarnings("serial")
public class PerryShiroToken implements AuthenticationToken {
  private final String token;

  public PerryShiroToken(String token) {
    if(token == null) {
      throw new AuthenticationException("token cannot be null.");
    }
    this.token = token;
  }

  @Override
  public Object getPrincipal() {
    return token;
  }

  @Override
  public Object getCredentials() {
    return token;
  }

  public String getToken() {
    return token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerryShiroToken)) {
      return false;
    }

    PerryShiroToken that = (PerryShiroToken) o;

    if (!token.equals(that.token)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return token.hashCode();
  }
}
