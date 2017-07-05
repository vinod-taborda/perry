package gov.ca.cwds;

import gov.ca.cwds.security.jwt.JwtConfiguration;
import gov.ca.cwds.service.scripts.IdentityMappingScript;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@ConfigurationProperties(prefix = "perry")
public class PerryProperties {
    @NestedConfigurationProperty
    private JwtConfiguration jwt;

    private Map<String, ServiceProviderConfiguration> serviceProviders = new HashMap<>();


    public static class ServiceProviderConfiguration {
        private String id;
        private IdentityMappingScript identityMapping;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public IdentityMappingScript getIdentityMapping() {
            return identityMapping;
        }

        public void setIdentityMapping(String identityMapping) throws IOException {
            this.identityMapping = new IdentityMappingScript(identityMapping);
        }
    }

    public JwtConfiguration getJwt() {
        return jwt;
    }

    public void setJwt(JwtConfiguration jwt) {
        this.jwt = jwt;
    }

    public Map<String, ServiceProviderConfiguration> getServiceProviders() {
        return serviceProviders;
    }

    public void setServiceProviders(Map<String, ServiceProviderConfiguration> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }
}
