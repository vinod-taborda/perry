package gov.ca.cwds.service;

import gov.ca.cwds.PerryProperties;
import gov.ca.cwds.rest.api.domain.auth.UserAuthorization;
import gov.ca.cwds.service.scripts.IdentityMappingScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import javax.transaction.Transactional;

/**
 * Created by dmitry.rudenko on 5/9/2017.
 */
@Service
@Transactional
public class IdentityMappingService {
    @Autowired
    private UserAuthorizationService userAuthorizationService;
    @Autowired
    private PerryProperties configuration;

    public String map(String subject, String providerId) {
        IdentityMappingScript mappingScript = loadMappingScriptForServiceProvider(providerId);
        if (mappingScript != null) {
            UserAuthorization authorization = userAuthorizationService.find(subject);
            try {
                return mappingScript.map(authorization);
            } catch (ScriptException e) {
                throw new RuntimeException("Identity Mapping failed for service provider: " + providerId, e);
            }
        }
        return subject;
    }


    private IdentityMappingScript loadMappingScriptForServiceProvider(String serviceProviderId) {
        if (serviceProviderId != null) {
            PerryProperties.ServiceProviderConfiguration spConfiguration = configuration.getServiceProviders().get(serviceProviderId);
            if (spConfiguration != null) {
                return spConfiguration.getIdentityMapping();
            }
        }
        return null;
    }
}
