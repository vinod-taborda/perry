package gov.ca.cwds.rest.api.domain.auth;

import java.util.HashMap;
import java.util.Map;


/**
 * Enumerated types for UnitAuthority
 *
 * @author CWDS API Team
 */
public enum UnitAuthority {


  /**
   * Supervisor All Assignment, Approval, Assignment Transfer Authorities
   */
  SUPERVISOR("S", "Supervisor"),

  /**
   * Approval Authority
   */
  APPROVAL_AUTHORITY("A", "Approval Authority"),

  /**
   * Assignment Transfer Authority
   */
  ASSIGNMENT_TRANSFER_AUTHORITY("T", "Assignment Transfer Authority"),

  /**
   * Both Approval and Assignment Transfer Authority but not a supervisor
   */
  BOTH_APPROVAL_AND_ASSIGNMENT_TRANSFER_AUTHORITY("B",
          "Both Approval and Assignment Transfer Authority"),

  /**
   * Unitwide Read
   */
  UNITWIDE_READ("R", "Unitwide Read"),

  /**
   * Unitwide Read/Write
   */
  UNITWIDE_READ_WRITE("U", "Unitwide Read/Write"),

  /**
   * None
   */
  NONE("N", "None");

  private static final Map<String, UnitAuthority> mapByUnitAuthorityCode = new HashMap<>();

  private final String code;
  private final String description;


  private UnitAuthority(String code, String description) {
    this.code = code;
    this.description = description;
  }


  public String getCode() {
    return this.code;
  }

  /**
   * Description
   *
   * @return description
   */
  public String getDescription() {
    return description;
  }


  public static UnitAuthority findByUnitAuthorityCode(String code) {
    return mapByUnitAuthorityCode.get(code);
  }


  static {
    for (UnitAuthority e : UnitAuthority.values()) {
      mapByUnitAuthorityCode.put(e.code, e);
    }
  }

}
