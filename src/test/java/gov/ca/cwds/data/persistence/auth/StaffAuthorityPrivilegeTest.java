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

import gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;


public class StaffAuthorityPrivilegeTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  public void equalsHashCodeWork() {
    EqualsVerifier.forClass(StaffAuthorityPrivilege.class).suppress(Warning.NONFINAL_FIELDS)
        .verify();
  }

  /*
   * Constructor test
   */
  @Test
  public void emtpyConstructorIsNotNull() throws Exception {
    assertThat(StaffAuthorityPrivilege.class.newInstance(), is(notNullValue()));
  }

  @Test
  public void persistentConstructorTest() throws Exception {
    StaffAuthorityPrivilege vsap = validStaffAuthorityPrivilege();

    StaffAuthorityPrivilege persistent = new StaffAuthorityPrivilege(vsap.getCountySpecificCode(),
        vsap.getEndDate(), vsap.getEndTime(), vsap.getFkuseridT(), vsap.getId(),
        vsap.getLevelOfAuthPrivilegeCode(), vsap.getLevelOfAuthPrivilegeType(), vsap.getStartDate(),
        vsap.getStartTime());

    assertThat(persistent.getCountySpecificCode(), is(equalTo(vsap.getCountySpecificCode())));
    assertThat(persistent.getEndDate(), is(equalTo(vsap.getEndDate())));
    assertThat(persistent.getEndTime(), is(equalTo(vsap.getEndTime())));
    assertThat(persistent.getFkuseridT(), is(equalTo(vsap.getFkuseridT())));
    assertThat(persistent.getId(), is(equalTo(vsap.getId())));
    assertThat(persistent.getLevelOfAuthPrivilegeCode(),
        is(equalTo(vsap.getLevelOfAuthPrivilegeCode())));
    assertThat(persistent.getLevelOfAuthPrivilegeType(),
        is(equalTo(vsap.getLevelOfAuthPrivilegeType())));
    assertThat(persistent.getStartDate(), is(equalTo(vsap.getStartDate())));
    assertThat(persistent.getStartTime(), is(equalTo(vsap.getStartTime())));

  }

  private StaffAuthorityPrivilege validStaffAuthorityPrivilege()
      throws JsonParseException, JsonMappingException, IOException {

    StaffAuthorityPrivilege validStaffAuthorityPrivilege =
        MAPPER.readValue(fixture("fixtures/domain/legacy/StaffAuthorityPrivilege/valid/valid.json"),
            StaffAuthorityPrivilege.class);

    return validStaffAuthorityPrivilege;

  }
}
