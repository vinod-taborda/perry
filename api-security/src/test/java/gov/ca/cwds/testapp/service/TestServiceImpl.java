package gov.ca.cwds.testapp.service;

import gov.ca.cwds.security.annotations.Authorize;
import gov.ca.cwds.testapp.domain.CaseDTO;

/**
 * Created by dmitry.rudenko on 10/6/2017.
 */
public class TestServiceImpl implements TestService {
  public void testArg( @Authorize("case:read:id") String id) {
    System.out.println();
  }

  @Override
  public void testCompositeObject(@Authorize("case:read:caseDTO.caseObject.id") CaseDTO caseDTO) {
    System.out.println();
  }

  @Override
  public void testCompositeObjectList(@Authorize("case:read:caseDTO.cases.id") CaseDTO caseDTO) {
    System.out.println();
  }
}
