package gov.ca.cwds.security.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * Created by dmitry.rudenko on 9/25/2017.
 */
public class AbacPermissionResolver implements PermissionResolver {

  @Override
  public Permission resolvePermission(String permissionString) {
    try {
      return new AbacPermission(permissionString);
    } catch (IllegalArgumentException e) {
      return new WildcardPermission(permissionString);
    }
  }
}
