package gov.ca.cwds.data.auth;


import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.auth.AssignmentUnit;
import gov.ca.cwds.inject.CmsSessionFactory;

import org.hibernate.SessionFactory;

import com.google.inject.Inject;

/**
 * DAO for {@link AssignmentUnit}.
 * 
 * @author CWDS API Team
 */
public class AssignmentUnitDao extends CrudsDaoImpl<AssignmentUnit> {

  /**
   * Constructor
   * 
   * @param sessionFactory The session factory
   */
  @Inject
  public AssignmentUnitDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}
