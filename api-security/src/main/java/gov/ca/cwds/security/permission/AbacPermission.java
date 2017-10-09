package gov.ca.cwds.security.permission;

import gov.ca.cwds.security.authorizer.Authorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * Created by dmitry.rudenko on 9/25/2017.
 */

public class AbacPermission extends WildcardPermission {
  private Object securedObject;
  private Authorizer authorizer;

  public AbacPermission(Object securedObject, Authorizer authorizer, String wildCard) {
    super(wildCard);
    this.securedObject = securedObject;
    this.authorizer = authorizer;
  }

  public AbacPermission() {
    super(AbacPermission.class.getName());
  }

  @Override
  public boolean implies(Permission permission) {
    if (!(permission instanceof AbacPermission)) {
      return false;
    }
    AbacPermission abacPermission = (AbacPermission) permission;
    return abacPermission.check();
  }

  private boolean check() {
    return securedObject != null
            && authorizer != null
            && authorizer.check(securedObject);
  }
}
