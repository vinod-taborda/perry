package gov.ca.cwds.testapp.domain;

/**
 * Created by dmitry.rudenko on 10/6/2017.
 */
public class Case {
  private Long id;
  private String name;

  public Case(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
