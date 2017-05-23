package gov.ca.cwds.data.auth;


import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.auth.AssignmentUnit;
import gov.ca.cwds.inject.CmsSessionFactory;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * DAO for {@link AssignmentUnit}.
 * 
 * @author CWDS API Team
 */
@Transactional
@Repository
public class AssignmentUnitDao extends CrudsDaoImpl<AssignmentUnit> {

  /**
   * Constructor
   * 
   * @param sessionFactory The session factory
   */
  public AssignmentUnitDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}
