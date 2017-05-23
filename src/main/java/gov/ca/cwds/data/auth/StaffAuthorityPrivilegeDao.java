package gov.ca.cwds.data.auth;

import com.google.inject.Inject;
import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege;
import gov.ca.cwds.inject.CmsSessionFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * DAO for {@link StaffAuthorityPrivilege}.
 * 
 * @author CWDS API Team
 */
@Transactional
@Repository
public class StaffAuthorityPrivilegeDao extends CrudsDaoImpl<StaffAuthorityPrivilege> {

  /**
   * Constructor
   * 
   * @param sessionFactory The session factory
   */
  @Inject
  public StaffAuthorityPrivilegeDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @SuppressWarnings("unchecked")
  public StaffAuthorityPrivilege[] findByUser(String userId) {
    Query query = this.getSessionFactory().getCurrentSession()
        .getNamedQuery("gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege.findByUser")
        .setString("userId", userId);
    return (StaffAuthorityPrivilege[]) query.list().toArray(new StaffAuthorityPrivilege[0]);
  }

  public StaffAuthorityPrivilege isSocialWorker(String userId) {
    Query query = this.getSessionFactory().getCurrentSession()
        .getNamedQuery(
            "gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege.checkForSocialWorker")
        .setString("userId", userId);
    return (StaffAuthorityPrivilege) query.uniqueResult();
  }

}
