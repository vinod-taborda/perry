package gov.ca.cwds.data.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import gov.ca.cwds.data.persistence.auth.AssignmentUnit;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hamcrest.junit.ExpectedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;

@Ignore
public class AssignmentUnitDaoIT {
  private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  private String startDateString = "1998-05-11";


  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static AssignmentUnitDao assignmentUnitDao;
  private static SessionFactory sessionFactory;
  private Session session;

  @BeforeClass
  public static void beforeClass() {
    sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    assignmentUnitDao = new AssignmentUnitDao(sessionFactory);
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
  public void testFind() {
    String id = "04SHs7W01z";
    AssignmentUnit found = assignmentUnitDao.find(id);
    assertThat(found.getId(), is(id));
  }

  @Test
  public void testCreate() throws Exception {
    Date startDate = df.parse(startDateString);
    AssignmentUnit assignmentUnit =
        new AssignmentUnit(new BigDecimal(1234567), 123, "A", "19", null, "NZGDRrd00E", "ABC",
            startDate, "04SHs7W01zA");
    AssignmentUnit created = assignmentUnitDao.create(assignmentUnit);
    assertThat(created, is(assignmentUnit));
  }

  @Test
  public void testCreateExistingEntityException() throws Exception {
    thrown.expect(EntityExistsException.class);
    Date startDate = df.parse(startDateString);
    AssignmentUnit assignmentUnit =
        new AssignmentUnit(new BigDecimal(1234567), 123, "A", "19", null, "NZGDRrd00E", "75D",
            startDate, "04SHs7W01z");
    assignmentUnitDao.create(assignmentUnit);
  }

  @Test
  public void testDelete() {
    String id = "04SHs7W01z";
    AssignmentUnit deleted = assignmentUnitDao.delete(id);
    assertThat(deleted.getId(), is(id));
  }

  @Test
  public void testUpdate() throws Exception {

    Date startDate = df.parse(startDateString);
    AssignmentUnit assignmentUnit =
        new AssignmentUnit(new BigDecimal(1234567), 123, "A", "19", null, "NZGDRrd00E", "75D",
            startDate, "04SHs7W01z");
    AssignmentUnit updated = assignmentUnitDao.update(assignmentUnit);
    assertThat(updated, is(assignmentUnit));
  }

  @Test
  public void testUpdateEntityNotFoundException() throws Exception {
    thrown.expect(EntityNotFoundException.class);

    Date startDate = df.parse(startDateString);
    AssignmentUnit assignmentUnit =
        new AssignmentUnit(new BigDecimal(1234567), 123, "A", "19", null, "NZGDRrd00E", "75D",
            startDate, "04SHs7W01zb");
    assignmentUnitDao.update(assignmentUnit);
  }
}
