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

import gov.ca.cwds.data.persistence.auth.UserId;
import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;


public class UserIdTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  public void equalsHashCodeWork() {
    EqualsVerifier.forClass(UserId.class).suppress(Warning.NONFINAL_FIELDS).verify();
  }

  /*
   * Constructor test
   */
  @Test
  public void emtpyConstructorIsNotNull() throws Exception {
    assertThat(UserId.class.newInstance(), is(notNullValue()));
  }

  @Test
  public void persistentConstructorTest() throws Exception {
    UserId vui = validUserId();

    UserId persistent = new UserId(vui.getEndDate(), vui.getEndTime(), vui.getFkfpstfprt(),
        vui.getStaffPersonId(), vui.getId(), vui.getLogonId(), vui.getSystemDomainType());

    assertThat(persistent.getEndDate(), is(equalTo(vui.getEndDate())));
    assertThat(persistent.getEndTime(), is(equalTo(vui.getEndTime())));
    assertThat(persistent.getFkfpstfprt(), is(equalTo(vui.getFkfpstfprt())));
    assertThat(persistent.getStaffPersonId(), is(equalTo(vui.getStaffPersonId())));
    assertThat(persistent.getId(), is(equalTo(vui.getId())));
    assertThat(persistent.getLogonId(), is(equalTo(vui.getLogonId())));
    assertThat(persistent.getSystemDomainType(), is(equalTo(vui.getSystemDomainType())));

  }

  private UserId validUserId() throws JsonParseException, JsonMappingException, IOException {

    UserId validUserId =
        MAPPER.readValue(fixture("fixtures/domain/legacy/UserId/valid/valid.json"), UserId.class);

    return validUserId;

  }
}
