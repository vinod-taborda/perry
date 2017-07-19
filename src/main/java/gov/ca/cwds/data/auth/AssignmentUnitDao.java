package gov.ca.cwds.data.auth;


import gov.ca.cwds.data.persistence.auth.AssignmentUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * DAO for {@link AssignmentUnit}.
 *
 * @author CWDS API Team
 */
@Transactional
@Repository
public interface AssignmentUnitDao extends JpaRepository<AssignmentUnit, String> {

}
