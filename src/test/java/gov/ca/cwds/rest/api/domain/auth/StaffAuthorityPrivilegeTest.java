package gov.ca.cwds.rest.api.domain.auth;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class StaffAuthorityPrivilegeTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  private String authPrivilegeType = "Countywide Read";
  private String authPrivilegeCode = "P";
  private String county = "Placer";

  /*
   * Constructor Tests
   */
  @Test
  public void jsonCreatorConstructorTest() throws Exception {

    StaffAuthorityPrivilege domain = new StaffAuthorityPrivilege("Countywide Read", "P", "Placer");

    assertThat(domain.getAuthPrivilegeType(), is(equalTo(authPrivilegeType)));
    assertThat(domain.getAuthPrivilegeCode(), is(equalTo(authPrivilegeCode)));
    assertThat(domain.getCounty(), is(equalTo(county)));
  }

  /*
   * Serialize the Json
   */
  @Test
  public void serializesToJSON() throws Exception {
    final String expected = MAPPER.writeValueAsString(
        MAPPER.readValue(fixture("fixtures/domain/auth/StaffAuthorityPrivilege/valid/valid.json"),
            StaffAuthorityPrivilege.class));

    assertThat(MAPPER.writeValueAsString(validStaffAuthorityPrivilege()), is(equalTo(expected)));
  }

  /*
   * Deserialize the Json
   */
  @Test
  public void deserializesFromJSON() throws Exception {
    assertThat(
        MAPPER.readValue(fixture("fixtures/domain/auth/StaffAuthorityPrivilege/valid/valid.json"),
            StaffAuthorityPrivilege.class),
        is(equalTo(validStaffAuthorityPrivilege())));
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
    return new StaffAuthorityPrivilege("Countywide Read", "P", "Placer");
  }

}
