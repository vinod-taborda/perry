package gov.ca.cwds.data.auth;

import gov.ca.cwds.data.persistence.auth.UserId;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO for {@link UserId}.
 *
 * @author CWDS API Team
 */

@Repository
public interface UserIdDao extends ReadOnlyRepository<UserId, String> {

  List<UserId> findByLogonId(String logonId);

}
