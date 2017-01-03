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

import gov.ca.cwds.data.persistence.auth.StaffUnitAuthority;

public class StaffUnitAuthorityDaoIT {
  private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  private String startDateString = "1998-05-11";


  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static StaffUnitAuthorityDao staffUnitAuthorityDao;
  private static SessionFactory sessionFactory;
  private Session session;

  @BeforeClass
  public static void beforeClass() {
    sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    staffUnitAuthorityDao = new StaffUnitAuthorityDao(sessionFactory);
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
  public void testfindUserFromstaffPersonIdNamedQueryExists() throws Exception {
    Query query =
        session.getNamedQuery("gov.ca.cwds.data.persistence.auth.StaffUnitAuthority.findByStaff")
            .setString("staffId", "75D");
    assertThat(query, is(notNullValue()));
  }


  @Test
  public void testFind() {
    String id = "AaiV6tm00E";
    StaffUnitAuthority found = staffUnitAuthorityDao.find(id);
    assertThat(found.getThirdId(), is(id));
  }

  @Test
  public void testCreate() throws Exception {
    Date startDate = df.parse(startDateString);
    StaffUnitAuthority staffUnitAuthority =
        new StaffUnitAuthority("A", "19", null, "NZGDRrd00E", "75D", startDate, "AaiV6tm00F");
    StaffUnitAuthority created = staffUnitAuthorityDao.create(staffUnitAuthority);
    assertThat(created, is(staffUnitAuthority));
  }

  @Test
  public void testCreateExistingEntityException() throws Exception {
    thrown.expect(EntityExistsException.class);
    Date startDate = df.parse(startDateString);
    StaffUnitAuthority staffUnitAuthority =
        new StaffUnitAuthority("A", "19", null, "NZGDRrd00E", "75D", startDate, "AaiV6tm00E");
    staffUnitAuthorityDao.create(staffUnitAuthority);
  }

  @Test
  public void testDelete() {
    String id = "AaiV6tm00E";
    StaffUnitAuthority deleted = staffUnitAuthorityDao.delete(id);
    assertThat(deleted.getThirdId(), is(id));
  }

  @Test
  public void testUpdate() throws Exception {

    Date startDate = df.parse(startDateString);
    StaffUnitAuthority staffUnitAuthority =
        new StaffUnitAuthority("A", "19", null, "NZGDRrd00E", "75D", startDate, "AaiV6tm00E");
    StaffUnitAuthority updated = staffUnitAuthorityDao.update(staffUnitAuthority);
    assertThat(updated, is(staffUnitAuthority));
  }

  @Test
  public void testUpdateEntityNotFoundException() throws Exception {
    thrown.expect(EntityNotFoundException.class);

    Date startDate = df.parse(startDateString);
    StaffUnitAuthority staffUnitAuthority =
        new StaffUnitAuthority("A", "19", null, "NZGDRrd00E", "75D", startDate, "AaiV6tm00G");
    staffUnitAuthorityDao.update(staffUnitAuthority);
  }
}
