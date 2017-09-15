package gov.ca.cwds.config;

import gov.ca.cwds.service.SAFService;
import gov.ca.cwds.service.oauth.SafUserInfoTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * Created by dmitry.rudenko on 5/23/2017.
 */
@Profile("prod")
@EnableOAuth2Sso
@Configuration
public class OAuthConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private ResourceServerProperties sso;
    @Autowired
    private SAFService safService;
    @Autowired
    private LoginServiceValidatorFilter loginServiceValidatorFilter;

    @Bean
    public SafUserInfoTokenService userInfoTokenServices() {
        return new SafUserInfoTokenService(safService, sso.getUserInfoUri(), sso.getClientId());
    }

    protected void configure(HttpSecurity http) throws Exception {
        //  /authn/validate should be for backend only!
        http.authorizeRequests().antMatchers("/authn/validate*", "/templates/**").permitAll()
                .and()
                .addFilterBefore(loginServiceValidatorFilter, AbstractPreAuthenticatedProcessingFilter.class);
        super.configure(http);
    }
}
