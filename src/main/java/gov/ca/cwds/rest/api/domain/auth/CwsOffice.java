package gov.ca.cwds.rest.api.domain.auth;

import io.dropwizard.jackson.JsonSnakeCase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;



/**
 * Class representing a Staff Person Authority Privilege.
 * 
 * @author CWDS API Team
 */
@ApiModel
@JsonSnakeCase
public class CwsOffice {

  @ApiModelProperty(example = "Office")
  @JsonProperty("office_id")
  private String officeId;

  @ApiModelProperty(example = "State")
  @JsonProperty("government_entity_type")
  private String governmentEntityType;

  @ApiModelProperty(example = "Fresno")
  @JsonProperty("county")
  private String county;


  /**
   * JSON Constructor
   * 
   * @param officeId the office Id
   * @param governmentEntityType the government entity type
   * @param county the county
   */
  public CwsOffice(@JsonProperty("office_id") String officeId,
      @JsonProperty("government_entity_type") String governmentEntityType,
      @JsonProperty("county") String county) {
    super();
    this.officeId = officeId;
    this.governmentEntityType = governmentEntityType;
    this.county = county;
  }



  /**
   * @return the officeId
   */
  public String getOfficeId() {
    return officeId;
  }



  /**
   * @return the governmentEntityType
   */
  public String getGovernmentEntityType() {
    return governmentEntityType;
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
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((county == null) ? 0 : county.hashCode());
    result =
        prime * result + ((governmentEntityType == null) ? 0 : governmentEntityType.hashCode());
    result = prime * result + ((officeId == null) ? 0 : officeId.hashCode());
    return result;
  }



  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(getClass().isInstance(obj))) {
      return false;
    }
    CwsOffice other = (CwsOffice) obj;
    if (county == null) {
      if (other.county != null) {
        return false;
      }
    } else if (!county.equals(other.county)) {
      return false;
    }
    if (governmentEntityType == null) {
      if (other.governmentEntityType != null) {
        return false;
      }
    } else if (!governmentEntityType.equals(other.governmentEntityType)) {
      return false;
    }
    if (officeId == null) {
      if (other.officeId != null) {
        return false;
      }
    } else if (!officeId.equals(other.officeId)) {
      return false;
    }
    return true;
  }


}
