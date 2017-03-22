package gov.ca.cwds.rest.api.domain.auth;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.ca.cwds.rest.core.Api;
import gov.ca.cwds.rest.resources.auth.UserAuthorizationResource;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;

public class UserAuthorizationTest {

  private static final String ROOT_RESOURCE = "/" + Api.RESOURCE_USER_AUTHORIZATION + "/";

  private static final UserAuthorizationResource mockedUserAuthorizationResource =
      mock(UserAuthorizationResource.class);

  private String userId = "userabc";
  private String staffPersonId = "q1p";
  private Boolean socialWorker = true;
  private Boolean supervisor = false;
  private Boolean overrideAuthority = true;
  private StaffUnitAuthority staffUnitAuthority = new StaffUnitAuthority("S", "ABC123", null, "21",
      null);
  private StaffAuthorityPrivilege authorityPrivilege = new StaffAuthorityPrivilege("1483", "P",
      "21", null);
  private CwsOffice cwsOffice = new CwsOffice("ABcDmKx00E", "1088", "21");
  private ImmutableSet<StaffUnitAuthority> testuserUnitAuthority = ImmutableSet
      .<StaffUnitAuthority>builder().add(staffUnitAuthority).build();
  private ImmutableSet<StaffAuthorityPrivilege> testuserAuthorityPrivilege = ImmutableSet
      .<StaffAuthorityPrivilege>builder().add(authorityPrivilege).build();
  private ImmutableSet<CwsOffice> testCwsOffice = ImmutableSet.<CwsOffice>builder().add(cwsOffice)
      .build();


  @ClassRule
  public static final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(mockedUserAuthorizationResource).build();


  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
  private UserAuthorization validUserAuthorization = validUserAuthorization();

  @Before
  public void setup() {
    when(mockedUserAuthorizationResource.create(eq(validUserAuthorization))).thenReturn(
        Response.status(Response.Status.NO_CONTENT).entity(null).build());

  }

  /*
   * Constructor Tests
   */

  @Test
  public void jsonCreatorConstructorTest() throws Exception {

    UserAuthorization domain =
        new UserAuthorization(userId, staffPersonId, socialWorker, supervisor, overrideAuthority,
            testuserAuthorityPrivilege, testuserUnitAuthority, testCwsOffice);

    assertThat(domain.getUserId(), is(equalTo(userId)));
    assertThat(domain.getStaffPersonId(), is(equalTo(staffPersonId)));
    assertThat(domain.getSupervisor(), is(equalTo(supervisor)));
    assertThat(domain.getOverrideAuthority(), is(equalTo(overrideAuthority)));
    assertThat(domain.getSocialWorker(), is(equalTo(socialWorker)));
    assertThat(domain.getAuthorityPrivilege(), is(equalTo(testuserAuthorityPrivilege)));
    assertThat(domain.getUnitAuthority(), is(equalTo(testuserUnitAuthority)));
    assertThat(domain.getCwsOffice(), is(equalTo(testCwsOffice)));
  }

  @Test
  public void serializesToJSON() throws Exception {
    final String expected =
        MAPPER.writeValueAsString(MAPPER.readValue(
            fixture("fixtures/domain/auth/UserAuthorization/valid/valid.json"),
            UserAuthorization.class));

    assertThat(MAPPER.writeValueAsString(validUserAuthorization()), is(equalTo(expected)));
  }

  @Test
  public void deserializesFromJSON() throws Exception {
    assertThat(MAPPER.readValue(fixture("fixtures/domain/auth/UserAuthorization/valid/valid.json"),
        UserAuthorization.class), is(equalTo(validUserAuthorization())));
  }

  @Test
  public void equalsHashCodeWork() {
    EqualsVerifier.forClass(UserAuthorization.class).suppress(Warning.NONFINAL_FIELDS).verify();
  }

  /*
   * Successful Tests
   */
  @Test
  public void successfulWithValid() throws Exception {
    UserAuthorization toCreate =
        MAPPER.readValue(fixture("fixtures/domain/auth/UserAuthorization/valid/valid.json"),
            UserAuthorization.class);
    Response response =
        resources.client().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
            .post(Entity.entity(toCreate, MediaType.APPLICATION_JSON));
    assertThat(response.getStatus(), is(equalTo(204)));
  }

  /*
   * Utils
   */
  private UserAuthorization validUserAuthorization() {
    return new UserAuthorization("userabc", "q1p", true, false, true, testuserAuthorityPrivilege,
        testuserUnitAuthority, testCwsOffice);
  }
}
