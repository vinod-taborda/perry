package gov.ca.cwds.service;

import gov.ca.cwds.PerryConfiguration;
import gov.ca.cwds.rest.api.domain.auth.UserAuthorization;
import gov.ca.cwds.service.scripts.IdentityMappingScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@Service
@Transactional
public class LoginService {
    @Autowired
    IdentityMappingService identityMappingService;
    @Autowired
    JwtService jwtService;
    @Autowired
    OAuth2ClientContext oauth2ClientContext;
    @Autowired
    SAFService safService;

    public String login(String providerId) throws Exception {
        String accessToken = oauth2ClientContext.getAccessToken().getValue();
        String userName = safService.getUserInfo();
        String jwtIdentity = identityMappingService.map(userName, providerId);
        return jwtService.generateToken(userName, accessToken, jwtIdentity);
    }
}
