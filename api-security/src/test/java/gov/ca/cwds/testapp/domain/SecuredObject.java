package gov.ca.cwds.testapp.domain;

/**
 * Created by dmitry.rudenko on 10/6/2017.
 */
public class SecuredObject {
  private Long id;
  private String field;

  public SecuredObject(Long id, String field) {
    this.id = id;
    this.field = field;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }
}
