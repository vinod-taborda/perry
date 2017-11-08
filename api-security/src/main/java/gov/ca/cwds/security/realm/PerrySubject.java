package gov.ca.cwds.security.realm;

import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;

public final class PerrySubject {

  private PerrySubject() {
    // no op
  }

  private static List getPrincipals() {
    return SecurityUtils.getSubject().getPrincipals().asList();
  }

  public static String getUser() {
    List principals = getPrincipals();
    if (principals.size() < 1) {
      throw new AuthorizationException("Incorrect principals: no User. Check Perry version and application security configuration.");
    }
    return (String) principals.get(0);
  }

  public static PerryAccount getPerryAccount() {
    List principals = getPrincipals();
    if (principals.size() < 2) {
      throw new AuthorizationException("Incorrect principals: no Perry Account. Check Perry version and application security configuration.");
    }
    return (PerryAccount) principals.get(1);
  }

  public static String getToken() {
    List principals = getPrincipals();
    if (principals.size() < 3) {
      throw new AuthorizationException("Incorrect principals: no token. Check Perry version and application security configuration.");
    }
    return (String) principals.get(2);
  }
}
