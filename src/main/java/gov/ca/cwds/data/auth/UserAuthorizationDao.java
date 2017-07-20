package gov.ca.cwds.data.auth;

import com.google.inject.Inject;
import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.auth.UserAuthorization;
import gov.ca.cwds.inject.CmsSessionFactory;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * DAO for {@link UserAuthorization}.
 * 
 * @author CWDS API Team
 */
@Transactional
@Repository
public interface UserAuthorizationDao extends ReadOnlyRepository<UserAuthorization, String> {

}
