package gov.ca.cwds.security.shiro.realms;

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
}
