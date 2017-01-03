package gov.ca.cwds.data.auth;

import org.hibernate.SessionFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.auth.UserAuthorization;
import gov.ca.cwds.inject.CmsSessionFactory;

/**
 * DAO for {@link UserAuthorization}.
 * 
 * @author CWDS API Team
 */
public class UserAuthorizationDao extends CrudsDaoImpl<UserAuthorization> {

  /**
   * Constructor
   * 
   * @param sessionFactory The session factory
   */
  @Inject
  public UserAuthorizationDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}
