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

public class CwsOfficeTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();


  private String officeId = "ABcDmKx00E";
  private String governmentEntityType = "1088";
  private String countyCode = "21";


  /*
   * Constructor Tests
   */

  @Test
  public void jsonCreatorConstructorTest() throws Exception {

    CwsOffice domain = new CwsOffice("ABcDmKx00E", "1088", "21");

    assertThat(domain.getOfficeId(), is(equalTo(officeId)));
    assertThat(domain.getGovernmentEntityType(), is(equalTo(governmentEntityType)));
    assertThat(domain.getCountyCode(), is(equalTo(countyCode)));
  }

  /*
   * Serialize the Json
   */
  @Test
  public void serializesToJSON() throws Exception {
    final String expected =
        MAPPER.writeValueAsString(MAPPER.readValue(
            fixture("fixtures/domain/auth/CwsOffice/valid/valid.json"), CwsOffice.class));

    assertThat(MAPPER.writeValueAsString(validCwsOffice()), is(equalTo(expected)));
  }

  /*
   * Deserialize the Json
   */
  @Test
  public void deserializesFromJSON() throws Exception {
    assertThat(MAPPER.readValue(fixture("fixtures/domain/auth/CwsOffice/valid/valid.json"),
        CwsOffice.class), is(equalTo(validCwsOffice())));
  }

  /*
   * Test for Equals has Code
   */
  @Test
  public void equalsHashCodeWork() {
    EqualsVerifier.forClass(CwsOffice.class).suppress(Warning.NONFINAL_FIELDS).verify();
  }

  /*
   * Utils
   */
  private CwsOffice validCwsOffice() {
    return new CwsOffice("ABcDmKx00E", "1088", "21");
  }
}
