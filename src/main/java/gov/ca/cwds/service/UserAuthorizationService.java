package gov.ca.cwds.service;

import com.google.inject.Inject;
import gov.ca.cwds.data.CrudsDao;
import gov.ca.cwds.data.auth.*;
import gov.ca.cwds.data.persistence.auth.UserId;
import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.domain.DomainChef;
import gov.ca.cwds.rest.api.domain.auth.CwsOffice;
import gov.ca.cwds.rest.api.domain.auth.StaffAuthorityPrivilege;
import gov.ca.cwds.rest.api.domain.auth.StaffUnitAuthority;
import gov.ca.cwds.rest.api.domain.auth.UserAuthorization;
import gov.ca.cwds.rest.services.CrudsService;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Business layer object to work on {@link UserAuthorization}
 * 
 * @author CWDS API Team
 */
@Transactional
@Service
public class UserAuthorizationService implements CrudsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizationService.class);

  @Autowired
  private UserIdDao userIdDao;
  @Autowired
  private StaffAuthorityPrivilegeDao staffAuthorityPrivilegeDao;
  @Autowired
  private StaffUnitAuthorityDao staffUnitAuthorityDao;
  @Autowired
  private CwsOfficeDao cwsOfficeDao;
  @Autowired
  private AssignmentUnitDao assignmentUnitDao;


  /**
   * {@inheritDoc}
   * 
   * @see CrudsService#find(Serializable)
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
   * @see CrudsService#delete(Serializable)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.auth.UserAuthorization delete(Serializable primaryKey) {
    throw new NotImplementedException("delete not implemented");
  }

  /**
   * {@inheritDoc}
   *
   * @see CrudsService#create(Request)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.auth.UserAuthorization create(Request request) {
    throw new NotImplementedException("create not implemented");
  }

  /**
   * {@inheritDoc}
   *
   * @see CrudsService#update(Serializable,
   *      Request)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.auth.UserAuthorization update(Serializable primaryKey,
      Request request) {
    throw new NotImplementedException("update not implemented");
  }

}
