package gov.ca.cwds.security.permission;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import gov.ca.cwds.security.authorizer.CaseAuthorizer;
import gov.ca.cwds.testapp.domain.Case;
import org.apache.shiro.authz.Permission;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

/**
 * @author CWDS TPT-3 Team
 */
public class AbacPermissionTest {

  private final AbacPermission testSubject = new AbacPermission();

  @Test
  public void implies_returnFalse_whenNotAbacPermissionInput() {
    // given
    final Permission input = p -> true;

    // when
    final boolean actualResult = testSubject.implies(input);

    // then
    assertThat(actualResult, is(false));
  }

  @Test
  public void implies_returnTrue_whenNoSecuredObjectInInput() {
    // given
    final AbacPermission input = new AbacPermission();

    // when
    final boolean actualResult = testSubject.implies(input);

    // then
    assertThat(actualResult, is(true));
  }

  @Test
  public void implies_returnFalse_whenNoAuthorizerInInput() {
    // given
    final AbacPermission input = new AbacPermission() {{
      setSecuredObject(new Case(1L, "name"));
    }};

    // when
    final boolean actualResult = testSubject.implies(input);

    // then
    assertThat(actualResult, is(false));
  }

  @Test
  public void implies_returnTrue_whenAuthorizedObjectInInput() {
    // given
    final AbacPermission input = new AbacPermission() {{
      setSecuredObject(new Case(1L, "name"));
      Whitebox.setInternalState(this, "authorizer", new CaseAuthorizer());
    }};

    // when
    final boolean actualResult = testSubject.implies(input);

    // then
    assertThat(actualResult, is(true));
  }

  @Test
  public void implies_returnFalse_whenNotAuthorizedObjectInInput() {
    // given
    final AbacPermission input = new AbacPermission() {{
      setSecuredObject(new Case(2L, "name"));
      Whitebox.setInternalState(this, "authorizer", new CaseAuthorizer());
    }};

    // when
    final boolean actualResult = testSubject.implies(input);

    // then
    assertThat(actualResult, is(false));
  }

}