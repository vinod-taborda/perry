package gov.ca.cwds.data.auth;


import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.auth.CwsOffice;
import gov.ca.cwds.inject.CmsSessionFactory;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;

/**
 * DAO for {@link CwsOffice}.
 * 
 * @author CWDS API Team
 */
public class CwsOfficeDao extends CrudsDaoImpl<CwsOffice> {

  /**
   * Constructor
   * 
   * @param sessionFactory The session factory
   */
  @Inject
  public CwsOfficeDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @SuppressWarnings("unchecked")
  public CwsOffice[] findByStaff(String staffId) {
    Query query =
        this.getSessionFactory().getCurrentSession()
            .getNamedQuery("gov.ca.cwds.data.persistence.auth.CwsOffice.findByStaff")
            .setString("staffId", staffId);
    return (CwsOffice[]) query.list().toArray(new CwsOffice[0]);

  }

}
