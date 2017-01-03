package gov.ca.cwds.data.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hamcrest.junit.ExpectedException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege;

public class StaffAuthorityPrivilegeDaoIT {
  private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  private DateFormat dt = new SimpleDateFormat("HH:mm:ss");
  private String startDateString = "1990-06-01";
  private String startTimeString = "14:34:00";



  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static StaffAuthorityPrivilegeDao staffAuthorityPrivilegeDao;
  private static SessionFactory sessionFactory;
  private Session session;

  @BeforeClass
  public static void beforeClass() {
    sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    staffAuthorityPrivilegeDao = new StaffAuthorityPrivilegeDao(sessionFactory);
  }

  @AfterClass
  public static void afterClass() {
    sessionFactory.close();
  }

  @Before
  public void setup() {
    session = sessionFactory.getCurrentSession();
    session.beginTransaction();
  }

  @After
  public void tearddown() {
    session.getTransaction().rollback();
  }


  @Test
  public void testcheckForSocialWorkerNamedQueryExists() throws Exception {
    Query query = session
        .getNamedQuery(
            "gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege.checkForSocialWorker")
        .setString("userId", "AfZi2k90Nj");
    assertThat(query, is(notNullValue()));
  }


  @Test
  public void testFind() {
    String id = "AiPPfrb00E";
    StaffAuthorityPrivilege found = staffAuthorityPrivilegeDao.find(id);
    assertThat(found.getId(), is(id));
  }

  @Test
  public void testCreate() throws Exception {
    Date startDate = df.parse(startDateString);
    Date startTime = dt.parse(startTimeString);
    StaffAuthorityPrivilege staffAuthorityPrivilege = new StaffAuthorityPrivilege("01", null, null,
        "HeXfJKx00E", "AiPPfrb00F", "P", (short) 1468, startDate, startTime);
    StaffAuthorityPrivilege created = staffAuthorityPrivilegeDao.create(staffAuthorityPrivilege);
    assertThat(created, is(staffAuthorityPrivilege));
  }

  @Test
  public void testCreateExistingEntityException() throws Exception {
    thrown.expect(EntityExistsException.class);
    Date startDate = df.parse(startDateString);
    Date startTime = dt.parse(startTimeString);
    StaffAuthorityPrivilege staffAuthorityPrivilege = new StaffAuthorityPrivilege("01", null, null,
        "HeXfJKx00E", "AiPPfrb00E", "P", (short) 1468, startDate, startTime);
    staffAuthorityPrivilegeDao.create(staffAuthorityPrivilege);
  }

  @Test
  public void testDelete() {
    String id = "AiPPfrb00E";
    StaffAuthorityPrivilege deleted = staffAuthorityPrivilegeDao.delete(id);
    assertThat(deleted.getId(), is(id));
  }

  @Test
  public void testUpdate() throws Exception {

    Date startDate = df.parse(startDateString);
    Date startTime = dt.parse(startTimeString);
    StaffAuthorityPrivilege staffAuthorityPrivilege = new StaffAuthorityPrivilege("01", null, null,
        "HeXfJKx00E", "AiPPfrb00E", "P", (short) 1468, startDate, startTime);
    StaffAuthorityPrivilege updated = staffAuthorityPrivilegeDao.update(staffAuthorityPrivilege);
    assertThat(updated, is(staffAuthorityPrivilege));
  }

  @Test
  public void testUpdateEntityNotFoundException() throws Exception {
    thrown.expect(EntityNotFoundException.class);

    Date startDate = df.parse(startDateString);
    Date startTime = dt.parse(startTimeString);
    StaffAuthorityPrivilege staffAuthorityPrivilege = new StaffAuthorityPrivilege("01", null, null,
        "HeXfJKx00E", "AiPPfrb00G", "P", (short) 1468, startDate, startTime);
    staffAuthorityPrivilegeDao.update(staffAuthorityPrivilege);
  }
}
