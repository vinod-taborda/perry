package gov.ca.cwds.security.authorizer;

import gov.ca.cwds.testapp.domain.Case;

/**
 * Created by dmitry.rudenko on 10/6/2017.
 */

public class CaseAuthorizer extends BaseAuthorizer<Case, Long> {
  public boolean checkId(Long id) {
    return id == 1L;
  }

  public boolean checkInstance(Case instance) {
    return this.checkId(instance.getId());
  }

  public Long stringToId(String id) {
    return Long.valueOf(id);
  }
}
