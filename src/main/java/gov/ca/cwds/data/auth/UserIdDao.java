package gov.ca.cwds.data.auth;

import gov.ca.cwds.data.persistence.auth.UserId;
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
public interface UserIdDao extends ReadOnlyRepository<UserId, String> {

  List<UserId> findByLogonId(String logonId);

}
