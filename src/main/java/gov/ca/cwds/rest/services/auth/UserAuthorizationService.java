package gov.ca.cwds.rest.services.auth;

import gov.ca.cwds.data.auth.CwsOfficeDao;
import gov.ca.cwds.data.auth.StaffAuthorityPrivilegeDao;
import gov.ca.cwds.data.auth.StaffUnitAuthorityDao;
import gov.ca.cwds.data.auth.UserIdDao;
import gov.ca.cwds.data.persistence.auth.UserId;
import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.domain.DomainChef;
import gov.ca.cwds.rest.api.domain.auth.CwsOffice;
import gov.ca.cwds.rest.api.domain.auth.StaffAuthorityPrivilege;
import gov.ca.cwds.rest.api.domain.auth.StaffUnitAuthority;
import gov.ca.cwds.rest.api.domain.auth.UserAuthorization;
import gov.ca.cwds.rest.services.CrudsService;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;


/**
 * Business layer object to work on {@link UserAuthorization}
 * 
 * @author CWDS API Team
 */
public class UserAuthorizationService implements CrudsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizationService.class);

  private UserIdDao userIdDao;
  private StaffAuthorityPrivilegeDao staffAuthorityPrivilegeDao;
  private StaffUnitAuthorityDao staffUnitAuthorityDao;
  private CwsOfficeDao cwsOfficeDao;

  /**
   * Constructor
   * 
   * @param userIdDao The User Id DAO
   * @param staffAuthorityPrivilegeDao The Staff Authority Privilege DAO
   * @param staffUnitAuthorityDao DAO for Staff Unit Authority
   * @param cwsOfficeDao DAO for CWS Office
   */
  @Inject
  public UserAuthorizationService(UserIdDao userIdDao,
      StaffAuthorityPrivilegeDao staffAuthorityPrivilegeDao,
      StaffUnitAuthorityDao staffUnitAuthorityDao, CwsOfficeDao cwsOfficeDao) {
    this.userIdDao = userIdDao;
    this.staffAuthorityPrivilegeDao = staffAuthorityPrivilegeDao;
    this.staffUnitAuthorityDao = staffUnitAuthorityDao;
    this.cwsOfficeDao = cwsOfficeDao;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#find(java.io.Serializable)
   */
  @Override
  public UserAuthorization find(Serializable primaryKey) {
    assert primaryKey instanceof String;
    LOGGER.info(primaryKey.toString());

    final String userId = ((String) primaryKey).trim();
    List<UserId> userList = userIdDao.listUserFromLogonId(userId);
    gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege socialWorker;

    // Found the user?
    if (userList != null && !userList.isEmpty()) {
      final UserId user = userList.get(0);
      socialWorker = staffAuthorityPrivilegeDao.isSocialWorker(user.getId());

      // Staff authorization privileges:
      final gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege[] staffAuthPrivs =
          this.staffAuthorityPrivilegeDao.findByUser(user.getId());

      Set<StaffAuthorityPrivilege> userAuthPrivs = new HashSet<>();
      for (gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege priv : staffAuthPrivs) {
        String endDate = DomainChef.cookDate(priv.getEndDate());
        userAuthPrivs.add(new StaffAuthorityPrivilege(
            priv.getLevelOfAuthPrivilegeType().toString(), priv.getLevelOfAuthPrivilegeCode(), priv
                .getCountySpecificCode(), endDate));
      }

      // Staff unit authorizations:
      final gov.ca.cwds.data.persistence.auth.StaffUnitAuthority[] staffUnitAuths =
          this.staffUnitAuthorityDao.findByStaff(user.getStaffPersonId());

      Set<StaffUnitAuthority> setStaffUnitAuths = new HashSet<>();
      for (gov.ca.cwds.data.persistence.auth.StaffUnitAuthority p : staffUnitAuths) {
        String endDate = DomainChef.cookDate(p.getEndDate());
        setStaffUnitAuths.add(new StaffUnitAuthority(p.getAuthorityCode(), p.getFkasgUnit(), p
            .getCountySpecificCode(), endDate));
      }

      final gov.ca.cwds.data.persistence.auth.CwsOffice[] cwsOffices =
          this.cwsOfficeDao.findByStaff(user.getStaffPersonId());

      Set<CwsOffice> setCwsOffices = new HashSet<>();
      for (gov.ca.cwds.data.persistence.auth.CwsOffice cwsOffice : cwsOffices) {
        setCwsOffices.add(new CwsOffice(cwsOffice.getOfficeId(), cwsOffice
            .getGovernmentEntityType().toString(), cwsOffice.getCountySpecificCode()));
      }
      return new UserAuthorization(user.getLogonId(), user.getStaffPersonId(),
          socialWorker != null, false, true, userAuthPrivs, setStaffUnitAuths, setCwsOffices);
    } else {
      LOGGER.warn("No user id found for " + primaryKey);
    }

    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#delete(java.io.Serializable)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.auth.UserAuthorization delete(Serializable primaryKey) {
    throw new NotImplementedException("delete not implemented");
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#create(gov.ca.cwds.rest.api.Request)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.auth.UserAuthorization create(Request request) {
    throw new NotImplementedException("create not implemented");
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#update(java.io.Serializable,
   *      gov.ca.cwds.rest.api.Request)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.auth.UserAuthorization update(Serializable primaryKey,
      Request request) {
    throw new NotImplementedException("update not implemented");
  }

}
