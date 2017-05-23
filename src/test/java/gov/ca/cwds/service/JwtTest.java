package gov.ca.cwds.service;

import gov.ca.cwds.PerryConfiguration;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
public class JwtTest {
    private PerryConfiguration configuration;
    private JwtService jwtService;
    private Set<String> roles;

    @Before
    public void before() {
        configuration = new PerryConfiguration();
        configuration.setJwt(new PerryConfiguration.JwtConfiguration());
        configuration.getJwt().setIssuer("setJwtIssuer");
        configuration.getJwt().setTimeout(30);
        configuration.setKeyStore(new PerryConfiguration.KeyStoreConfiguration());
        configuration.getKeyStore().setAlias("test");
        configuration.getKeyStore().setPassword("test");
        configuration.getKeyStore().setKeyPassword("test");
        configuration.getKeyStore().setPath(getClass().getResource("/test.jks").getPath());
        jwtService = new JwtService();
        jwtService.oAuth2ClientContext = new DefaultOAuth2ClientContext(new DefaultOAuth2AccessToken("accessToken"));
        jwtService.perryConfiguration = configuration;
        roles = new HashSet<>();
        roles.add("role1");
        roles.add("role2");
    }
    @Test
    public void testGenerateValidate() throws Exception {
        String token = jwtService.generateToken("test", "accessToken", "{'test':'test'}");
        String identity = jwtService.validateToken(token);
        Assert.assertEquals("{'test':'test'}", identity);
    }
}
