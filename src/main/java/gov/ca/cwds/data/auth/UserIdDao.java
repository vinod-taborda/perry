package gov.ca.cwds.data.auth;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.auth.UserId;
import gov.ca.cwds.inject.CmsSessionFactory;

/**
 * DAO for {@link UserId}.
 * 
 * @author CWDS API Team
 */
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
