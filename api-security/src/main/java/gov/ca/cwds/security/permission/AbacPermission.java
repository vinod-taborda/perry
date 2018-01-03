package gov.ca.cwds.security.permission;

import com.google.inject.Key;
import com.google.inject.name.Names;
import gov.ca.cwds.security.authorizer.Authorizer;
import gov.ca.cwds.security.authorizer.BaseAuthorizer;
import gov.ca.cwds.security.module.SecurityModule;
import org.apache.shiro.authz.Permission;

import java.util.Optional;

/**
 * Created by dmitry.rudenko on 9/25/2017.
 */

public class AbacPermission implements Permission {
  private Object securedObject;
  private Authorizer authorizer;

  private static final String SUPPORTED_PERMISSION_PATTERN = "^[^:,*]+:[^:,*]+:[^:,*]+$";
  private static final String PARTS_DELIMITER = ":";

  public AbacPermission() {

  }

  public AbacPermission(String permissionString) {
    if (permissionString.matches(SUPPORTED_PERMISSION_PATTERN)) {
      String[] parts = permissionString.split(PARTS_DELIMITER);
      String handlerName = parts[0] + PARTS_DELIMITER + parts[1];
      Optional<Authorizer> permissionHandler = findPermissionHandler(handlerName);
      if (!permissionHandler.isPresent()) {
        throw new IllegalArgumentException();
      }
      this.securedObject = parts[2];
      this.authorizer = permissionHandler.get();
    } else {
      throw new IllegalArgumentException();
    }
  }

  public Object getSecuredObject() {
    return securedObject;
  }

  public void setSecuredObject(Object securedObject) {
    this.securedObject = securedObject;
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

  private Optional<Authorizer> findPermissionHandler(String name) {
    try {
      Authorizer authorizer = SecurityModule.injector().getInstance(Key.get(Authorizer.class, Names.named(name)));
      return Optional.of(authorizer);
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
