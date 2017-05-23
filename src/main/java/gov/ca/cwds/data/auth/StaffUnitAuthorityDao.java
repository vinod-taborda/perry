package gov.ca.cwds.data.auth;


import com.google.inject.Inject;
import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.auth.StaffUnitAuthority;
import gov.ca.cwds.inject.CmsSessionFactory;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * DAO for {@link StaffUnitAuthority}.
 * 
 * @author CWDS API Team
 */
@Transactional
@Repository
public class StaffUnitAuthorityDao extends CrudsDaoImpl<StaffUnitAuthority> {

  /**
   * Constructor
   * 
   * @param sessionFactory The session factory
   */
  @Inject
  public StaffUnitAuthorityDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @SuppressWarnings("unchecked")
  public StaffUnitAuthority[] findByStaff(String staffId) {
    Query query = this.getSessionFactory().getCurrentSession()
        .getNamedQuery("gov.ca.cwds.data.persistence.auth.StaffUnitAuthority.findByStaff")
        .setString("staffId", staffId);
    return (StaffUnitAuthority[]) query.list().toArray(new StaffUnitAuthority[0]);

  }

}
