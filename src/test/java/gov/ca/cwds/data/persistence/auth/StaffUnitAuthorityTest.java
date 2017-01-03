package gov.ca.cwds.data.persistence.auth;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.cwds.data.persistence.auth.StaffUnitAuthority;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;


public class StaffUnitAuthorityTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  public void equalsHashCodeWork() {
    EqualsVerifier.forClass(StaffUnitAuthority.class).suppress(Warning.NONFINAL_FIELDS).verify();
  }

  /*
   * Constructor test
   */
  @Test
  public void emtpyConstructorIsNotNull() throws Exception {
    assertThat(StaffUnitAuthority.class.newInstance(), is(notNullValue()));
  }

  @Test
  public void persistentConstructorTest() throws Exception {
    StaffUnitAuthority vsua = validStaffUnitAuthority();

    StaffUnitAuthority persistent = new StaffUnitAuthority(vsua.getAuthorityCode(),
        vsua.getCountySpecificCode(), vsua.getEndDate(), vsua.getFkasgUnit(),
        vsua.getStaffPersonId(), vsua.getStartDate(), vsua.getThirdId());

    assertThat(persistent.getAuthorityCode(), is(equalTo(vsua.getAuthorityCode())));
    assertThat(persistent.getCountySpecificCode(), is(equalTo(vsua.getCountySpecificCode())));
    assertThat(persistent.getEndDate(), is(equalTo(vsua.getEndDate())));
    assertThat(persistent.getFkasgUnit(), is(equalTo(vsua.getFkasgUnit())));
    assertThat(persistent.getStaffPersonId(), is(equalTo(vsua.getStaffPersonId())));
    assertThat(persistent.getStartDate(), is(equalTo(vsua.getStartDate())));
    assertThat(persistent.getThirdId(), is(equalTo(vsua.getThirdId())));

  }

  private StaffUnitAuthority validStaffUnitAuthority()
      throws JsonParseException, JsonMappingException, IOException {

    StaffUnitAuthority validStaffUnitAuthority =
        MAPPER.readValue(fixture("fixtures/domain/legacy/StaffUnitAuthority/valid/valid.json"),
            StaffUnitAuthority.class);

    return validStaffUnitAuthority;

  }
}
