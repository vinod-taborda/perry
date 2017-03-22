package gov.ca.cwds.rest.api.domain.auth;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StaffAuthorityPrivilegeTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  private String authPrivilegeType = "1483";
  private String authPrivilegeCode = "P";
  private String countyCode = "21";

  /*
   * Constructor Tests
   */
  @Test
  public void jsonCreatorConstructorTest() throws Exception {

    StaffAuthorityPrivilege domain = new StaffAuthorityPrivilege("1483", "P", "21", null);

    assertThat(domain.getAuthPrivilegeType(), is(equalTo(authPrivilegeType)));
    assertThat(domain.getAuthPrivilegeCode(), is(equalTo(authPrivilegeCode)));
    assertThat(domain.getCountyCode(), is(equalTo(countyCode)));
  }

  /*
   * Serialize the Json
   */
  @Test
  public void serializesToJSON() throws Exception {
    final String expected =
        MAPPER.writeValueAsString(MAPPER.readValue(
            fixture("fixtures/domain/auth/StaffAuthorityPrivilege/valid/valid.json"),
            StaffAuthorityPrivilege.class));

    assertThat(MAPPER.writeValueAsString(validStaffAuthorityPrivilege()), is(equalTo(expected)));
  }

  /*
   * Deserialize the Json
   */
  @Test
  public void deserializesFromJSON() throws Exception {
    assertThat(MAPPER.readValue(
        fixture("fixtures/domain/auth/StaffAuthorityPrivilege/valid/valid.json"),
        StaffAuthorityPrivilege.class), is(equalTo(validStaffAuthorityPrivilege())));
  }

  /*
   * Test for Equals has Code
   */
  @Test
  public void equalsHashCodeWork() {
    EqualsVerifier.forClass(UserAuthorization.class).suppress(Warning.NONFINAL_FIELDS).verify();
  }

  /*
   * Utils
   */
  private StaffAuthorityPrivilege validStaffAuthorityPrivilege() {
    return new StaffAuthorityPrivilege("1483", "P", "21", null);
  }

}
