package gov.ca.cwds.security.utils;

import gov.ca.cwds.security.realm.PerryAccount;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.Optional;

/**
 * @author CWDS TPT-3 Team
 */
public final class PrincipalUtils {

  public static final String DEFAULT_USER_ID = "0X5";

  private PrincipalUtils() {
  }

  public static PerryAccount getPrincipal() {
    Optional<PerryAccount> perryAccount = Optional.empty();
    Subject currentUser = SecurityUtils.getSubject();
    if (currentUser.getPrincipals() != null) {
      perryAccount = currentUser.getPrincipals().asList().stream()
          .filter(principal -> principal instanceof PerryAccount).findAny();
    }
    return perryAccount.orElseThrow(IllegalStateException::new);
  }

  public static String getStaffPersonId() {
    PerryAccount perryAccount = getPrincipal();
    if (perryAccount.getStaffId() == null || perryAccount.getStaffId().isEmpty()) {
      return DEFAULT_USER_ID;
    }
    return perryAccount.getStaffId();
  }

}
