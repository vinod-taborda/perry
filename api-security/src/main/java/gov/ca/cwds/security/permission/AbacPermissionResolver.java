package gov.ca.cwds.security.permission;

import com.google.inject.Key;
import com.google.inject.name.Names;
import gov.ca.cwds.rest.BaseApiApplication;
import gov.ca.cwds.security.authorizer.Authorizer;
import gov.ca.cwds.security.permission.AbacPermission;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Optional;

/**
 * Created by dmitry.rudenko on 9/25/2017.
 */
public class AbacPermissionResolver implements PermissionResolver {
  private static final String SUPPORTED_PERMISSION_PATTERN = "^[^:,*]+:[^:,*]+:[^:,*]+$";
  private static final String PARTS_DELIMITER = ":";

  @Override
  public Permission resolvePermission(String permissionString) {
    if (permissionString.matches(SUPPORTED_PERMISSION_PATTERN)) {
      String[] parts = permissionString.split(PARTS_DELIMITER);
      String handlerName = parts[0] + PARTS_DELIMITER + parts[1];
      Optional<Authorizer> permissionHandler = findPermissionHandler(handlerName);
      if (permissionHandler.isPresent()) {
        return new AbacPermission(parts[2], permissionHandler.get(), permissionString);
      }
    }
    return new WildcardPermission(permissionString);
  }

  private Optional<Authorizer> findPermissionHandler(String name) {
    try {
      Authorizer authorizer = BaseApiApplication.getInjector().getInstance(Key.get(Authorizer.class, Names.named(name)));
      return Optional.of(authorizer);
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
