package gov.ca.cwds.data.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hamcrest.junit.ExpectedException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;

import gov.ca.cwds.data.persistence.auth.UserId;

@Ignore
public class UserIdDaoIT {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private static UserIdDao userIdDao;
  private static SessionFactory sessionFactory;
  private Session session;

  @BeforeClass
  public static void beforeClass() {
    sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    userIdDao = new UserIdDao(sessionFactory);
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
  public void testfindUserFromLogonIdNamedQueryExists() throws Exception {
    Query query =
        session.getNamedQuery("gov.ca.cwds.data.persistence.auth.UserId.findUserFromLogonId")
            .setString("logonId", "MCALLUM");
    assertThat(query, is(notNullValue()));
  }


  @Test
  public void testFind() {
    String id = "Aaf6x8c00E";
    UserId found = userIdDao.find(id);
    assertThat(found.getId(), is(id));
  }

  @Test
  public void testCreate() throws Exception {
    UserId userId = new UserId(null, null, null, "75D", "Aaf6x8c00F", "MCALLUM", (short) 5394);
    UserId created = userIdDao.create(userId);
    assertThat(created, is(userId));
  }

  @Test
  public void testCreateExistingEntityException() throws Exception {
    thrown.expect(EntityExistsException.class);
    UserId userId = new UserId(null, null, null, "75D", "Aaf6x8c00E", "MCALLUM", (short) 5394);
    userIdDao.create(userId);
  }

  @Test
  public void testDelete() {
    String id = "Aaf6x8c00E";
    UserId deleted = userIdDao.delete(id);
    assertThat(deleted.getId(), is(id));
  }

  @Test
  public void testUpdate() throws Exception {

    UserId userId = new UserId(null, null, null, "75D", "Aaf6x8c00E", "MCALLUM", (short) 5394);
    UserId updated = userIdDao.update(userId);
    assertThat(updated, is(userId));
  }

  @Test
  public void testUpdateEntityNotFoundException() throws Exception {
    thrown.expect(EntityNotFoundException.class);

    UserId userId = new UserId(null, null, null, "75D", "Aaf6x8c00G", "MCALLUM", (short) 5394);
    userIdDao.update(userId);
  }
}
