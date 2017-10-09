package gov.ca.cwds.testapp.service;

import gov.ca.cwds.testapp.domain.CaseDTO;

/**
 * Created by dmitry.rudenko on 10/6/2017.
 */
public interface TestService {
   void testArg(String id);

   void testCompositeObject(CaseDTO caseDTO);

   void testCompositeObjectList(CaseDTO caseDTO);
}
