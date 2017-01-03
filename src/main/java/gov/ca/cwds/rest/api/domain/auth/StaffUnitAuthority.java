package gov.ca.cwds.rest.api.domain.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.jackson.JsonSnakeCase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * Class representing a Staff Person Unit of Authority.
 * 
 * @author CWDS API Team
 */
@ApiModel
@JsonSnakeCase
public class StaffUnitAuthority {

  @ApiModelProperty(example = "Unitwide Read")
  @JsonProperty("unit_authority_type")
  private String unitAuthorityType;

  @ApiModelProperty(example = "O2ABcDe00F")
  @JsonProperty("assigned_unit")
  private String assignedUnit;

  @ApiModelProperty(example = "Sacramento")
  @JsonProperty("county")
  private String county;

  /**
   * JSON Constructor
   * 
   * @param unitAuthorityType the unit authority
   * @param assignedUnit the assigned unit
   * @param county the county
   */
  public StaffUnitAuthority(@JsonProperty("unit_authority_type") String unitAuthorityType,
      @JsonProperty("assigned_unit") String assignedUnit, @JsonProperty("county") String county) {
    super();
    this.unitAuthorityType = unitAuthorityType;
    this.assignedUnit = assignedUnit;
    this.county = county;
  }

  /**
   * @return the unitAuthorityType
   */
  public String getUnitAuthorityType() {
    return unitAuthorityType;
  }

  /**
   * @return the assignedUnit
   */
  public String getAssignedUnit() {
    return assignedUnit;
  }

  /**
   * @return the county
   */
  public String getCounty() {
    return county;
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((assignedUnit == null) ? 0 : assignedUnit.hashCode());
    result = prime * result + ((county == null) ? 0 : county.hashCode());
    result = prime * result + ((unitAuthorityType == null) ? 0 : unitAuthorityType.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    StaffUnitAuthority other = (StaffUnitAuthority) obj;
    if (assignedUnit == null) {
      if (other.assignedUnit != null) {
        return false;
      }
    } else if (!assignedUnit.equals(other.assignedUnit)) {
      return false;
    }
    if (county == null) {
      if (other.county != null) {
        return false;
      }
    } else if (!county.equals(other.county)) {
      return false;
    }
    if (unitAuthorityType == null) {
      if (other.unitAuthorityType != null) {
        return false;
      }
    } else if (!unitAuthorityType.equals(other.unitAuthorityType)) {
      return false;
    }
    return true;
  }

}
