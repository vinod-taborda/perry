package gov.ca.cwds.security.authorizer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import gov.ca.cwds.testapp.domain.Case;
import org.apache.shiro.authz.AuthorizationException;
import org.junit.Test;

/**
 * Created by TPT2 on 10/12/2017.
 */
public class BaseAuthorizerTest {
  private static final BaseAuthorizer<String, Long> baseAuthorizer = new BaseAuthorizer<String, Long>() {};
  private static final BaseAuthorizer concreteAuthorizer = new CaseAuthorizer();

  public static class CaseSubclass extends Case {
    public CaseSubclass(Long id, String name) {
      super(id, name);
    }
  }

  @Test(expected = AuthorizationException.class)
  public void testCheckIdNotImplemented() {
    baseAuthorizer.checkId(1L);
  }

  @Test(expected = AuthorizationException.class)
  public void testCheckInstanceNotImplemented() {
    baseAuthorizer.checkInstance("");
  }

  @Test(expected = AuthorizationException.class)
  public void testCheckStringToIdNotImplemented() {
    baseAuthorizer.stringToId("1");
  }

  @Test
  public void check_success_whenSubclassInput() {
    // given
    final CaseSubclass input = new CaseSubclass(1L, "name");

    // when
    final boolean actualValue = concreteAuthorizer.check(input);

    // then
    assertThat(actualValue, is(true));
  }

  @Test
  public void check_success_whenOriginalClassInput() {
    // given
    final Case input = new Case(1L, "name");

    // when
    final boolean actualValue = concreteAuthorizer.check(input);

    // then
    assertThat(actualValue, is(true));
  }

  @Test
  public void check_success_whenExpectedIdType() {
    assertThat(
        concreteAuthorizer.check(1L),
        is(true)
    );
  }

  @Test
  public void check_success_whenIdTypeIsString() {
    assertThat(
        concreteAuthorizer.check("1"),
        is(true)
    );
  }

  @Test(expected = AuthorizationException.class)
  public void check_exception_whenUnknownClassInput() {
    concreteAuthorizer.check(new Integer("100"));
  }
}
