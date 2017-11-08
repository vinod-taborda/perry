package gov.ca.cwds.data.auth;


import gov.ca.cwds.data.persistence.auth.AssignmentUnit;
import org.springframework.stereotype.Repository;

/**
 * DAO for {@link AssignmentUnit}.
 *
 * @author CWDS API Team
 */

@Repository
public interface AssignmentUnitDao extends ReadOnlyRepository<AssignmentUnit, String> {

}
