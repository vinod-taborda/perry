package gov.ca.cwds.data.auth;


import gov.ca.cwds.data.persistence.auth.StaffUnitAuthority;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * DAO for {@link StaffUnitAuthority}.
 *
 * @author CWDS API Team
 */
@Transactional
@Repository
public interface StaffUnitAuthorityDao extends ReadOnlyRepository<StaffUnitAuthority, String> {

  List<StaffUnitAuthority> findByStaffPersonId(String staffPersonId);
}
