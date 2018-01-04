package gov.ca.cwds.security.realm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

/**
 * Created by dmitry.rudenko on 6/2/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerryAccount {
    @JsonProperty
    private String user;
    @JsonProperty
    private Set<String> roles;
    @JsonProperty
    private String staffId;
    @JsonProperty("county_code")
    private String countyCode;
    @JsonProperty("county_cws_code")
    private String countyCwsCode;
    @JsonProperty("county_name")
    private String countyName;
    @JsonProperty("government_entity_type")
    private String governmentEntityType;
    @JsonProperty
    private Set<String> privileges;

    public PerryAccount(String user) {
        this.user = user;
    }

    /**
     * Default Constructor
     */
    public PerryAccount() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyCwsCode() {
        return countyCwsCode;
    }

    public void setCountyCwsCode(String countyCwsCode) {
        this.countyCwsCode = countyCwsCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getGovernmentEntityType() {
        return governmentEntityType;
    }

    public void setGovernmentEntityType(String governmentEntityType) {
        this.governmentEntityType = governmentEntityType;
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<String> privileges) {
        this.privileges = privileges;
    }
}
