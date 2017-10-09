package gov.ca.cwds.testapp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry.rudenko on 10/9/2017.
 */
public class CaseDTO {
  private Case caseObject;
  private List<Case> cases = new ArrayList<>();

  public Case getCaseObject() {
    return caseObject;
  }

  public void setCaseObject(Case caseObject) {
    this.caseObject = caseObject;
  }

  public List<Case> getCases() {
    return cases;
  }

  public void setCases(List<Case> cases) {
    this.cases = cases;
  }
}
