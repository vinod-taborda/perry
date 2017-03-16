package gov.ca.cwds.data.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import gov.ca.cwds.data.persistence.auth.CwsOffice;

import java.math.BigDecimal;

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

public class CwsOfficeDaoIT {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static CwsOfficeDao cwsOfficeDao;
  private static SessionFactory sessionFactory;
  private Session session;

  @BeforeClass
  public static void beforeClass() {
    sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    cwsOfficeDao = new CwsOfficeDao(sessionFactory);
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
  public void testfindCwsOfficeFromstaffPersonIdNamedQueryExists() throws Exception {
    Query query =
        session.getNamedQuery("gov.ca.cwds.data.persistence.auth.CwsOffice.findByStaff").setString(
            "staffId", "75D");
    assertThat(query, is(notNullValue()));
  }


  @Test
  public void testFind() {
    String id = "AiKZr9u00E";
    CwsOffice found = cwsOfficeDao.find(id);
    assertThat(found.getOfficeId(), is(id));
  }

  @Test
  public void testCreate() throws Exception {
    CwsOffice cwsOffice =
        new CwsOffice("ABpWmKx00E", new BigDecimal(0), "19", (short) 1088, "N", "N", " ",
            new BigDecimal(0), 0, " ", new BigDecimal(0), 0, "75D", " ", "  ", " ", " ", "12 ",
            (short) 0, (short) 0, " ");

    CwsOffice created = cwsOfficeDao.create(cwsOffice);
    assertThat(created, is(cwsOffice));
  }

  @Test
  public void testCreateExistingEntityException() throws Exception {
    thrown.expect(EntityExistsException.class);
    CwsOffice cwsOffice =
        new CwsOffice("AiKZr9u00E", new BigDecimal(0), "19", (short) 1088, "N", "N", " ",
            new BigDecimal(0), 0, " ", new BigDecimal(0), 0, "75D", " ", "  ", " ", " ", "12 ",
            (short) 0, (short) 0, " ");
    cwsOfficeDao.create(cwsOffice);
  }

  @Test
  public void testDelete() {
    String id = "Am73NsQ00E";
    CwsOffice deleted = cwsOfficeDao.delete(id);
    assertThat(deleted.getOfficeId(), is(id));
  }

  @Test
  public void testUpdate() throws Exception {

    CwsOffice cwsOffice =
        new CwsOffice("Am73NsQ00E", new BigDecimal(0), "19", (short) 1088, "N", "N", " ",
            new BigDecimal(0), 0, " ", new BigDecimal(0), 0, "75D", " ", "  ", " ", " ", "12 ",
            (short) 0, (short) 0, " ");
    CwsOffice updated = cwsOfficeDao.update(cwsOffice);
    assertThat(updated, is(cwsOffice));
  }

  @Test
  public void testUpdateEntityNotFoundException() throws Exception {
    thrown.expect(EntityNotFoundException.class);

    CwsOffice cwsOffice =
        new CwsOffice("ABpWmKx00G", new BigDecimal(0), "19", (short) 1088, "N", "N", " ",
            new BigDecimal(0), 0, " ", new BigDecimal(0), 0, "75D", " ", "  ", " ", " ", "12 ",
            (short) 0, (short) 0, " ");
    cwsOfficeDao.update(cwsOffice);
  }
}
