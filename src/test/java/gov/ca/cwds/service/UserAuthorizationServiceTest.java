package gov.ca.cwds.service;

import gov.ca.cwds.data.auth.*;
import gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege;
import gov.ca.cwds.data.persistence.auth.StaffPerson;
import gov.ca.cwds.data.persistence.auth.UserId;
import gov.ca.cwds.rest.api.domain.auth.UserAuthorization;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

/**
 * Created by dmitry.rudenko on 10/3/2017.
 */
public class UserAuthorizationServiceTest {
  @Test
  public void test() {
    UserIdDao userIdDao = Mockito.mock(UserIdDao.class);
    StaffAuthorityPrivilegeDao staffAuthorityPrivilegeDao = Mockito.mock(StaffAuthorityPrivilegeDao.class);
    StaffUnitAuthorityDao staffUnitAuthorityDao = Mockito.mock(StaffUnitAuthorityDao.class);
    CwsOfficeDao cwsOfficeDao = Mockito.mock(CwsOfficeDao.class);
    AssignmentUnitDao assignmentUnitDao = Mockito.mock(AssignmentUnitDao.class);
    StaffPersonDao staffPersonDao = Mockito.mock(StaffPersonDao.class);

    UserAuthorizationService userAuthorizationService = new UserAuthorizationService();
    userAuthorizationService.setAssignmentUnitDao(assignmentUnitDao);
    userAuthorizationService.setCwsOfficeDao(cwsOfficeDao);
    userAuthorizationService.setStaffAuthorityPrivilegeDao(staffAuthorityPrivilegeDao);
    userAuthorizationService.setUserIdDao(userIdDao);
    userAuthorizationService.setStaffUnitAuthorityDao(staffUnitAuthorityDao);
    userAuthorizationService.setStaffPersonDao(staffPersonDao);

    String racfid = "racfid";

    UserId userId = Mockito.mock(UserId.class);
    Mockito.when(userId.getId()).thenReturn("id");
    Mockito.when(userId.getLogonId()).thenReturn("logonid");
    Mockito.when(userId.getStaffPersonId()).thenReturn("staffId");

    Mockito.when(userIdDao.findByLogonId(racfid))
            .thenReturn(Collections.singletonList(userId));

    Mockito.when(staffAuthorityPrivilegeDao.findSocialWorkerPrivileges(userId.getId()))
            .thenReturn(Collections.emptyList());

    StaffPerson staffPerson = Mockito.mock(StaffPerson.class);
    Mockito.when(staffPerson.getCountyCode()).thenReturn("12");
    Mockito.when(staffPerson.getId()).thenReturn("staffId");
    Mockito.when(staffPersonDao.findOne("staffId")).thenReturn(staffPerson);

    StaffAuthorityPrivilege staffAuthorityPrivilege = Mockito.mock(StaffAuthorityPrivilege.class);
    Mockito.when(staffAuthorityPrivilege.getCountySpecificCode()).thenReturn("12");
    Mockito.when(staffAuthorityPrivilege.getLevelOfAuthPrivilegeCode()).thenReturn("L");
    Mockito.when(staffAuthorityPrivilege.getLevelOfAuthPrivilegeType()).thenReturn((short) 1);


    Mockito.when(staffAuthorityPrivilegeDao.findByFkuseridT(userId.getId()))
            .thenReturn(Collections.singletonList(staffAuthorityPrivilege));

    UserAuthorization userAuthorization = userAuthorizationService.find(racfid);
    assert userAuthorization.getUserId().equals("logonid");
    assert !userAuthorization.getSocialWorker();
    assert userAuthorization.getStaffPerson() != null;
    assert userAuthorization.getStaffPerson().getId().equals("staffId");
    assert userAuthorization.getAuthorityPrivilege().size() == 1;
    gov.ca.cwds.rest.api.domain.auth.StaffAuthorityPrivilege authorityPrivilege = userAuthorization.getAuthorityPrivilege().iterator().next();
    assert authorityPrivilege.getAuthPrivilegeCode().equals("L");
    assert authorityPrivilege.getAuthPrivilegeType().equals("1");
    assert authorityPrivilege.getCounty().equals("Humboldt");
    assert authorityPrivilege.getCountyCode().equals("12");
    assert authorityPrivilege.getAuthPrivilegeCodeDesc().equals("Staff Person Level of Auth Type");

  }
}
