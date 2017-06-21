package gov.ca.cwds.service.oauth;

import gov.ca.cwds.service.SAFService;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.List;
import java.util.Map;

/**
 * Created by dmitry.rudenko on 5/30/2017.
 */
public class SafUserInfoTokenService extends UserInfoTokenServices {
    private SAFService safService;
    private String clientId;

    public SafUserInfoTokenService(SAFService safService, String userInfoEndpointUrl, String clientId) {
        super(userInfoEndpointUrl, clientId);
        this.clientId = clientId;
        this.safService = safService;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) {
        Map userInfo = safService.getUserInfo(accessToken);
        String userName = (String) userInfo.get("username");
        OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null,
                null, null, null, null);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userName, "N/A", extractAuthorities(userInfo));
        token.setDetails(userInfo);
        return new OAuth2Authentication(request, token);
    }

    private List<GrantedAuthority> extractAuthorities(Map map) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
    }

}
