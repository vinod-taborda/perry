package gov.ca.cwds.data.auth;

import com.google.inject.Inject;
import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.auth.UserId;
import gov.ca.cwds.inject.CmsSessionFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * DAO for {@link UserId}.
 * 
 * @author CWDS API Team
 */
@Transactional
@Repository
public class UserIdDao extends CrudsDaoImpl<UserId> {

  /**
   * Constructor
   * 
   * @param sessionFactory The session factory
   */
  @Inject
  public UserIdDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @SuppressWarnings("unchecked")
  public List<UserId> listUserFromLogonId(String logonId) {
    Query query = this.getSessionFactory().getCurrentSession()
        .getNamedQuery("gov.ca.cwds.data.persistence.auth.UserId.findUserFromLogonId")
        .setString("logonId", logonId);
    return query.list();
  }

}
