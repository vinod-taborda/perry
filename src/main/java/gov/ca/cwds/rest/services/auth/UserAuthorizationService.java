package gov.ca.cwds.rest.services.auth;

import gov.ca.cwds.data.auth.AssignmentUnitDao;
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
import org.apache.commons.lang3.StringUtils;
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
  private AssignmentUnitDao assignmentUnitDao;

  /**
   * Constructor
   * 
   * @param userIdDao The User Id DAO
   * @param staffAuthorityPrivilegeDao The Staff Authority Privilege DAO
   * @param staffUnitAuthorityDao DAO for Staff Unit Authority
   * @param cwsOfficeDao DAO for CWS Office
   * @param assignmentUnitDao DAO for Assignment Unit
   */
  @Inject
  public UserAuthorizationService(UserIdDao userIdDao,
      StaffAuthorityPrivilegeDao staffAuthorityPrivilegeDao,
      StaffUnitAuthorityDao staffUnitAuthorityDao, CwsOfficeDao cwsOfficeDao,
      AssignmentUnitDao assignmentUnitDao) {
    this.userIdDao = userIdDao;
    this.staffAuthorityPrivilegeDao = staffAuthorityPrivilegeDao;
    this.staffUnitAuthorityDao = staffUnitAuthorityDao;
    this.cwsOfficeDao = cwsOfficeDao;
    this.assignmentUnitDao = assignmentUnitDao;
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

    if (userList != null && !userList.isEmpty()) {
      final UserId user = userList.get(0);
      String userIdentifier = user.getId();
      String staffPersonIdentifier = user.getStaffPersonId();
      socialWorker = staffAuthorityPrivilegeDao.isSocialWorker(userIdentifier);

      Set<StaffAuthorityPrivilege> userAuthPrivs = getStaffAuthorityPriveleges(userIdentifier);

      Set<StaffUnitAuthority> setStaffUnitAuths = getStaffUnitAuthorities(staffPersonIdentifier);

      Set<CwsOffice> setCwsOffices = getCwsOffices(staffPersonIdentifier);

      return new UserAuthorization(user.getLogonId(), user.getStaffPersonId(),
          socialWorker != null, false, true, userAuthPrivs, setStaffUnitAuths, setCwsOffices);
    } else {
      LOGGER.warn("No user id found for " + primaryKey);
    }

    return null;
  }

  /**
   * Gets the {@link CwsOffice} for a Staff Person
   * 
   * @param staffPersonId The Staff Person Id
   * @return Set of CwsOffice for the Staff Person
   */
  private Set<CwsOffice> getCwsOffices(String staffPersonId) {

    Set<CwsOffice> setCwsOffices = new HashSet<>();
    final gov.ca.cwds.data.persistence.auth.CwsOffice[] cwsOffices =
        this.cwsOfficeDao.findByStaff(staffPersonId);


    for (gov.ca.cwds.data.persistence.auth.CwsOffice cwsOffice : cwsOffices) {
      setCwsOffices.add(new CwsOffice(cwsOffice.getOfficeId(), cwsOffice.getGovernmentEntityType()
          .toString(), cwsOffice.getCountySpecificCode()));
    }
    return setCwsOffices;
  }

  /**
   * Gets the {@link StaffUnitAuthority} for a StaffPerson
   * 
   * @param staffPersonId The Staff Person Id
   * @return Set of StaffUnitAuthority for the Staff Person
   */
  private Set<StaffUnitAuthority> getStaffUnitAuthorities(String staffPersonId) {

    Set<StaffUnitAuthority> setStaffUnitAuths = new HashSet<>();
    final gov.ca.cwds.data.persistence.auth.StaffUnitAuthority[] staffUnitAuths =
        this.staffUnitAuthorityDao.findByStaff(staffPersonId);

    for (gov.ca.cwds.data.persistence.auth.StaffUnitAuthority staffUnitAuth : staffUnitAuths) {
      String endDate = DomainChef.cookDate(staffUnitAuth.getEndDate());
      String assignedUnitKey = staffUnitAuth.getFkasgUnit().trim();
      String assignedUnitEndDate = "";
      if (StringUtils.isNotBlank(assignedUnitKey)) {
        final gov.ca.cwds.data.persistence.auth.AssignmentUnit assignmentUnit =
            this.assignmentUnitDao.find(assignedUnitKey);
        assignedUnitEndDate = DomainChef.cookDate(assignmentUnit.getEndDate());
      }
      setStaffUnitAuths.add(new StaffUnitAuthority(staffUnitAuth.getAuthorityCode(),
          assignedUnitKey, assignedUnitEndDate, staffUnitAuth.getCountySpecificCode(), endDate));
    }
    return setStaffUnitAuths;
  }

  /**
   * Gets the {@link StaffAuthorityPrivilege} for a User
   * 
   * @param userId the User Identifier
   * @return Set of StaffAuthorityPrivilege for the User
   */
  private Set<StaffAuthorityPrivilege> getStaffAuthorityPriveleges(String userId) {

    Set<StaffAuthorityPrivilege> userAuthPrivs = new HashSet<>();
    final gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege[] staffAuthPrivs =
        this.staffAuthorityPrivilegeDao.findByUser(userId);
    for (gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege priv : staffAuthPrivs) {
      String endDate = DomainChef.cookDate(priv.getEndDate());
      userAuthPrivs.add(new StaffAuthorityPrivilege(priv.getLevelOfAuthPrivilegeType().toString(),
          priv.getLevelOfAuthPrivilegeCode(), priv.getCountySpecificCode(), endDate));
    }
    return userAuthPrivs;
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
