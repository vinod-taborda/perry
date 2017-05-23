package gov.ca.cwds;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.ca.cwds.service.scripts.IdentityMappingScript;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@ConfigurationProperties(prefix = "perry")
public class PerryConfiguration {
    @NestedConfigurationProperty
    private JwtConfiguration jwt;
    @NestedConfigurationProperty
    private KeyStoreConfiguration keyStore;
    @NestedConfigurationProperty
    private SafConfiguration saf;
    private Map<String, ServiceProviderConfiguration> serviceProviders = new HashMap<>();

    public static class JwtConfiguration {
        private int timeout;
        private String issuer;

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }
    }

    public static class SafConfiguration {
       private String userInfoUrl;

        public String getUserInfoUrl() {
            return userInfoUrl;
        }

        public void setUserInfoUrl(String userInfoUrl) {
            this.userInfoUrl = userInfoUrl;
        }
    }

    public static class KeyStoreConfiguration {
        private String path;
        private String alias;
        private String password;
        private String keyPassword;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getKeyPassword() {
            return keyPassword;
        }

        public void setKeyPassword(String keyPassword) {
            this.keyPassword = keyPassword;
        }
    }

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


    public KeyStoreConfiguration getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(KeyStoreConfiguration keyStore) {
        this.keyStore = keyStore;
    }

    public JwtConfiguration getJwt() {
        return jwt;
    }

    public void setJwt(JwtConfiguration jwt) {
        this.jwt = jwt;
    }

    public SafConfiguration getSaf() {
        return saf;
    }

    public void setSaf(SafConfiguration saf) {
        this.saf = saf;
    }

    public Map<String, ServiceProviderConfiguration> getServiceProviders() {
        return serviceProviders;
    }

    public void setServiceProviders(Map<String, ServiceProviderConfiguration> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }
}
