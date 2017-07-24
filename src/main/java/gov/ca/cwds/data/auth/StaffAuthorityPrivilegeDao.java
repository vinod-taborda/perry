package gov.ca.cwds.data.auth;

import gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * DAO for {@link StaffAuthorityPrivilege}.
 *
 * @author CWDS API Team
 */
@Transactional
@Repository
public interface StaffAuthorityPrivilegeDao extends ReadOnlyRepository<StaffAuthorityPrivilege, String> {

  List<StaffAuthorityPrivilege> findByFkuseridT(String userId);

  @Query("SELECT S FROM StaffAuthorityPrivilege S WHERE S.fkuseridT = :userId AND "
          + "S.levelOfAuthPrivilegeType = '1468' AND "
          + "S.levelOfAuthPrivilegeCode = 'P' AND S.endDate is null")
  List<StaffAuthorityPrivilege> findSocialWorkerPrivileges(@Param("userId") String userId);

}
