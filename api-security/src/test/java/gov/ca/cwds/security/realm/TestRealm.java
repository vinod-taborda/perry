package gov.ca.cwds.security.realm;

import gov.ca.cwds.security.permission.AbacPermission;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by dmitry.rudenko on 9/22/2017.
 */
public class TestRealm extends JwtRealm {
  private static SimpleAuthorizationInfo authorizationInfo;
  static {
    authorizationInfo = new SimpleAuthorizationInfo();
    authorizationInfo.addObjectPermission(new AbacPermission());
  }

  public static void addPermission(String permission) {
    authorizationInfo.addStringPermission(permission);
  }

  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    return authorizationInfo;
  }

}
