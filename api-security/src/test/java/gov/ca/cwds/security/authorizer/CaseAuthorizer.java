package gov.ca.cwds.security.authorizer;

import gov.ca.cwds.testapp.domain.Case;

/**
 * Created by dmitry.rudenko on 10/6/2017.
 */

public class CaseAuthorizer extends Authorizer<Case, Long> {
  public boolean checkId(Long id) {
    return id == 1L;
  }

  public Long stringToId(String id) {
    return Long.valueOf(id);
  }
}
