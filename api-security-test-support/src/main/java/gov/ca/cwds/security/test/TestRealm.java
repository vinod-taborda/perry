package gov.ca.cwds.security.test;

import gov.ca.cwds.security.permission.AbacPermission;
import gov.ca.cwds.security.realm.PerryAccount;
import java.util.Collection;
import java.util.Collections;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * @author CWDS CALS API Team
 */

public class TestRealm extends AuthorizingRealm {

  public static final String REALM_NAME = "testRealm";

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    return new AuthorizationInfo() {
      @Override
      public Collection<String> getRoles() {
        return Collections.EMPTY_LIST;
      }

      @Override
      public Collection<String> getStringPermissions() {
        return Collections.EMPTY_LIST;
      }

      @Override
      public Collection<Permission> getObjectPermissions() {
        return Collections.singletonList(new AbacPermission());
      }
    };
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
          throws AuthenticationException {
    final PerryAccount principal = (PerryAccount) token.getPrincipal();
    return new AuthenticationInfo() {
      @Override
      public PrincipalCollection getPrincipals() {
        SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
        principalCollection.add(principal.getUser(), REALM_NAME);
        principalCollection.add(principal, REALM_NAME);
        return principalCollection;
      }

      @Override
      public Object getCredentials() {
        return token.getCredentials();
      }
    };
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return true;
  }

  @Override
  protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
    //Empty
  }

}
