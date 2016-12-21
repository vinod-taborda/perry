package gov.ca.cwds.perry;

import java.util.ArrayList;
import java.util.List;

public class Employee {

  private Integer id;
  private String name;
  private Integer age;
  private Integer salary;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Integer getSalary() {
    return salary;
  }

  public void setSalary(Integer salary) {
    this.salary = salary;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Employee other = (Employee) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }


  public static final List<Employee> EMPLOYEES = new ArrayList<>();
  static {
    Employee e1 = new Employee();
    e1.setId(100);
    e1.setName("Mike");
    e1.setAge(27);
    e1.setSalary(5000);

    Employee e2 = new Employee();
    e2.setId(101);
    e2.setName("Randy");
    e2.setAge(25);
    e2.setSalary(5000);

    EMPLOYEES.add(e1);

    EMPLOYEES.add(e2);

  }
}
