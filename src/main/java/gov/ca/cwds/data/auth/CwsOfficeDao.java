package gov.ca.cwds.data.auth;


import gov.ca.cwds.data.persistence.auth.CwsOffice;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * DAO for {@link CwsOffice}.
 *
 * @author CWDS API Team
 */
@Transactional
@Repository
public interface CwsOfficeDao extends ReadOnlyRepository<CwsOffice, String> {

  List<CwsOffice> findByStaffPersonId(String staffPersonId);

}
