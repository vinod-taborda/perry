package gov.ca.cwds.rest.api.domain.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.jackson.JsonSnakeCase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;



/**
 * Class representing a CWS Office.
 * 
 * @author CWDS API Team
 */
@ApiModel
@JsonSnakeCase
public class CwsOffice {

  private static final Logger LOGGER = LoggerFactory.getLogger(CwsOffice.class);

  @ApiModelProperty(example = "Office")
  @JsonProperty("office_id")
  private String officeId;

  @ApiModelProperty(example = "1011")
  @JsonProperty("government_entity_type")
  private String governmentEntityType;

  @ApiModelProperty(example = "State of California")
  @JsonProperty("government_entity_type_desc")
  private String governmentEntityTypeDesc;

  @ApiModelProperty(example = "21")
  @JsonProperty("county_code")
  private String countyCode;

  @ApiModelProperty(example = "Marin")
  @JsonProperty("county")
  private String county;


  /**
   * JSON Constructor
   * 
   * @param officeId the office Id
   * @param governmentEntityType the government entity type
   * @param countyCode the county code
   */
  public CwsOffice(@JsonProperty("office_id") String officeId,
      @JsonProperty("government_entity_type") String governmentEntityType,
      @JsonProperty("county_code") String countyCode) {
    super();
    this.officeId = officeId;
    this.governmentEntityType = governmentEntityType;
    this.governmentEntityTypeDesc = getGovernmentEntityTypeDescription(governmentEntityType);
    this.countyCode = countyCode;
    this.county = GovernmentEntityType.findByCountyCd(countyCode).getDescription();
  }



  private String getGovernmentEntityTypeDescription(String type) {
    String description = "";
    try {
      Integer sysId = Integer.parseInt(type);
      description = GovernmentEntityType.getGovernmentEntityTypeBySysId(sysId).getDescription();
    } catch (Exception e) {
      LOGGER.error("ERROR - Description couldn't parse", e.getMessage(), e);
    }
    return description;
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
   * @return the governmentEntityTypeDesc
   */
  public String getGovernmentEntityTypeDesc() {
    return governmentEntityTypeDesc;
  }



  /**
   * @return the county
   */
  public String getCountyCode() {
    return countyCode;
  }



  /**
   * @return the countyDesc
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
    result = prime * result + ((countyCode == null) ? 0 : countyCode.hashCode());
    result = prime * result + ((county == null) ? 0 : county.hashCode());
    result =
        prime * result + ((governmentEntityType == null) ? 0 : governmentEntityType.hashCode());
    result = prime * result
        + ((governmentEntityTypeDesc == null) ? 0 : governmentEntityTypeDesc.hashCode());
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
    if (countyCode == null) {
      if (other.countyCode != null) {
        return false;
      }
    } else if (!countyCode.equals(other.countyCode)) {
      return false;
    }
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
    if (governmentEntityTypeDesc == null) {
      if (other.governmentEntityTypeDesc != null) {
        return false;
      }
    } else if (!governmentEntityTypeDesc.equals(other.governmentEntityTypeDesc)) {
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
