package gov.ca.cwds.service;

import gov.ca.cwds.PerryProperties;
import gov.ca.cwds.rest.api.domain.auth.UserAuthorization;
import gov.ca.cwds.service.scripts.IdentityMappingScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import javax.transaction.Transactional;

/**
 * Created by dmitry.rudenko on 5/9/2017.
 */
@Profile("prod")
@Service
@Transactional
public class IdentityMappingService {

    private static final String DEFAULT_SP_ID_NAME = "default";

    @Autowired
    private UserAuthorizationService userAuthorizationService;
    @Autowired
    private PerryProperties configuration;

    public String map(String subject, String providerId) {
        IdentityMappingScript mappingScript = loadMappingScriptForServiceProvider(providerId);
        if (mappingScript != null) {
            UserAuthorization authorization = userAuthorizationService.find(subject);
            if(authorization != null) {
                try {
                    return mappingScript.map(authorization);
                } catch (ScriptException e) {
                    throw new RuntimeException("Identity Mapping failed for service provider: " + providerId, e);
                }
            }
        }
        return subject;
    }


    private IdentityMappingScript loadMappingScriptForServiceProvider(String serviceProviderId) {
        return loadScript(serviceProviderId == null ? DEFAULT_SP_ID_NAME : serviceProviderId);
    }

    private IdentityMappingScript loadScript(String serviceProviderId) {
        PerryProperties.ServiceProviderConfiguration spConfiguration = configuration.getServiceProviders().get(serviceProviderId);
        if(spConfiguration != null) {
            return spConfiguration.getIdentityMapping();
        }
        return null;
    }
}
