package gov.ca.cwds.rest.api.domain.auth;

import io.dropwizard.jackson.JsonSnakeCase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Class representing a Staff Person Unit of Authority.
 * 
 * @author CWDS API Team
 */
@ApiModel
@JsonSnakeCase
public class StaffUnitAuthority {

  @ApiModelProperty(example = "R")
  @JsonProperty("unit_authority_code")
  private String unitAuthorityCode;

  @ApiModelProperty(example = "Unitwide Read")
  @JsonProperty("unit_authority_code_desc")
  private String unitAuthorityCodeDesc;

  @ApiModelProperty(example = "O2ABcDe00F")
  @JsonProperty("assigned_unit")
  private String assignedUnit;

  @ApiModelProperty(example = "21")
  @JsonProperty("county_code")
  private String countyCode;

  @ApiModelProperty(example = "Marin")
  @JsonProperty("county")
  private String county;


  /**
   * JSON Constructor
   * 
   * @param unitAuthorityType the unit authority
   * @param assignedUnit the assigned unit
   * @param countyCode the county code
   */
  public StaffUnitAuthority(@JsonProperty("unit_authority_code") String unitAuthorityType,
      @JsonProperty("assigned_unit") String assignedUnit,
      @JsonProperty("county_code") String countyCode) {
    super();
    this.unitAuthorityCode = unitAuthorityType;
    this.unitAuthorityCodeDesc =
        UnitAuthority.findByUnitAuthorityCode(unitAuthorityType).getDescription();
    this.assignedUnit = assignedUnit;
    this.countyCode = countyCode;
    this.county = GovernmentEntityType.findByCountyCd(countyCode).getDescription();
  }


  /**
   * @return the unitAuthorityType
   */
  public String getUnitAuthorityCode() {
    return unitAuthorityCode;
  }


  /**
   * @return the unitAuthorityTypeDesc
   */
  public String getUnitAuthorityCodeDesc() {
    return unitAuthorityCodeDesc;
  }


  /**
   * @return the assignedUnit
   */
  public String getAssignedUnit() {
    return assignedUnit;
  }


  /**
   * @return the countyCode
   */
  public String getCountyCode() {
    return countyCode;
  }


  /**
   * @return the county
   */
  public String getCounty() {
    return county;
  }


  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((assignedUnit == null) ? 0 : assignedUnit.hashCode());
    result = prime * result + ((county == null) ? 0 : county.hashCode());
    result = prime * result + ((countyCode == null) ? 0 : countyCode.hashCode());
    result = prime * result + ((unitAuthorityCode == null) ? 0 : unitAuthorityCode.hashCode());
    result =
        prime * result + ((unitAuthorityCodeDesc == null) ? 0 : unitAuthorityCodeDesc.hashCode());
    return result;
  }


  /*
   * (non-Javadoc)
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
    if (countyCode == null) {
      if (other.countyCode != null) {
        return false;
      }
    } else if (!countyCode.equals(other.countyCode)) {
      return false;
    }
    if (unitAuthorityCode == null) {
      if (other.unitAuthorityCode != null) {
        return false;
      }
    } else if (!unitAuthorityCode.equals(other.unitAuthorityCode)) {
      return false;
    }
    if (unitAuthorityCodeDesc == null) {
      if (other.unitAuthorityCodeDesc != null) {
        return false;
      }
    } else if (!unitAuthorityCodeDesc.equals(other.unitAuthorityCodeDesc)) {
      return false;
    }
    return true;
  }


}
