package gov.ca.cwds.service;

import gov.ca.cwds.UniversalUserToken;
import gov.ca.cwds.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@Profile("prod")
@Service
@Transactional
public class OauthLoginService implements LoginService {
    @Autowired
    IdentityMappingService identityMappingService;
    @Autowired
    JwtService jwtService;
    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    public String login(String providerId) throws Exception {
        UniversalUserToken userToken = (UniversalUserToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accessToken = oauth2ClientContext.getAccessToken().getValue();
        String jwtIdentity = identityMappingService.map(userToken, providerId);
        return jwtService.generate(accessToken, userToken.getUserId(), jwtIdentity);
    }

    public String validate(String token) throws Exception {
        return jwtService.validate(token);
    }
}
