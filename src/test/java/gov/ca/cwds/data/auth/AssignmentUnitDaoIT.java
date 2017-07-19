package gov.ca.cwds.data.auth;

import gov.ca.cwds.data.persistence.auth.AssignmentUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringRunner.class)
@DataJpaTest(excludeAutoConfiguration = {FlywayAutoConfiguration.class})
@DirtiesContext
@ActiveProfiles("dev")
public class AssignmentUnitDaoIT {
  private Date startDate = new Date();

  @Autowired
  private AssignmentUnitDao assignmentUnitDao;

  @Test
  public void testFind() {
    String id = "testFind";
    AssignmentUnit assignmentUnit = entity(id);
    assignmentUnitDao.save(assignmentUnit);
    AssignmentUnit found = assignmentUnitDao.findOne(id);
    assertThat(found.getId(), is(id));
  }

  @Test
  public void testCreate() throws Exception {
    String id = "04SHs7W01zA";
    AssignmentUnit assignmentUnit = entity(id);
    AssignmentUnit created = assignmentUnitDao.save(assignmentUnit);
    assertThat(created, is(assignmentUnit));
  }

  @Test
  public void testDelete() {
    String id = "testDelete";
    AssignmentUnit assignmentUnit = entity(id);
    assignmentUnitDao.save(assignmentUnit);
    AssignmentUnit found = assignmentUnitDao.findOne(id);
    assertThat(found.getId(), is(id));
    assignmentUnitDao.delete(id);
    found = assignmentUnitDao.findOne(id);
    assertThat(found, is(nullValue()));
  }

  @Test
  public void testUpdate() throws Exception {
    String id = "testUpdate";
    BigDecimal newPhone = new BigDecimal(666);
    AssignmentUnit assignmentUnit = entity(id);
    assignmentUnitDao.save(assignmentUnit);
    AssignmentUnit found = assignmentUnitDao.findOne(id);
    assertThat(found.getPhoneNo(), is(new BigDecimal(1234567)));
    AssignmentUnit newOne = entity(id, newPhone);
    assignmentUnitDao.save(newOne);
    found = assignmentUnitDao.findOne(id);
    assertThat(found.getPhoneNo(), is(newPhone));
  }

  private AssignmentUnit entity(String id) {
    return new AssignmentUnit(new BigDecimal(1234567), 123, "A", "19", null, "NZGDRrd00E", "75D",
            startDate, id);
  }

  private AssignmentUnit entity(String id, BigDecimal phone) {
    return new AssignmentUnit(phone, 123, "A", "19", null, "NZGDRrd00E", "75D",
            startDate, id);
  }
}
