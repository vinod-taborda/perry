package gov.ca.cwds.security.shiro.realms;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.ca.cwds.security.shiro.PerryShiroToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CWDS CALS API Team
 */
public abstract class AbstractRealm extends AuthorizingRealm {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRealm.class);

  /**
   * primary principal equals to username secondary principal equals to user token
   */
  private static final int PRINCIPALS_COUNT = 2;

  private ObjectMapper objectMapper;

  public AbstractRealm() {
    setAuthenticationTokenClass(PerryShiroToken.class);
  }

  @Override
  protected void onInit() {
    super.onInit();
    objectMapper = new ObjectMapper();
  }

  /**
   *
   * @param token
   * @return Result of validation (authorization data in JSON format)
   */
  protected abstract String validate(String token) throws AuthenticationException;

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    List principalsList = principals.asList();
    if (principalsList.size() == PRINCIPALS_COUNT) {
      PerryAccount perryAccount = (PerryAccount) principalsList.get(1);
      return new SimpleAuthorizationInfo(perryAccount.getRoles());
    }
    throw new AuthenticationException("User authorization failed!");
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
    String tokenString = ((PerryShiroToken) token).getToken();
    String json = validate(tokenString);

    PerryAccount perryAccount = map(json);
    return getAuthenticationInfo(perryAccount);
  }

  /**
   * Maps payload to user info. For more complex user info override this method. User info will
   * be accessible as secondary principal:
   * <p>
   * Subject subject = SecurityUtils.getSubject(); List principals =
   * subject.getPrincipals().asList(); PerryAccount account = (PerryAccount) principals.get(1);
   *
   * @param json jwt payload
   * @return mapped jwt payload
   */
  protected PerryAccount map(String json) {
    try {
      return objectMapper.readValue(json, PerryAccount.class);
    } catch (IOException e) {
      LOGGER.info(e.getMessage(), e);
      // Mapping doesn't apply
      return new PerryAccount() {
        {
          setUser(json);
        }
      };
    }
  }

  private AuthenticationInfo getAuthenticationInfo(PerryAccount perryAccount) {
    List<Object> principals = new ArrayList<>();
    principals.add(perryAccount.getUser());
    principals.add(perryAccount);
    PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, getName());
    return new SimpleAuthenticationInfo(principalCollection, "N/A");
  }

}
