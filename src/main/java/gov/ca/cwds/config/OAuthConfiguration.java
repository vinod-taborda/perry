package gov.ca.cwds.config;

import gov.ca.cwds.service.OauthLogoutService;
import gov.ca.cwds.service.SAFService;
import gov.ca.cwds.service.oauth.SafUserInfoTokenService;
import gov.ca.cwds.web.OAuthLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
  @Autowired
  private OauthLogoutService tokenRevocationLogoutHandler;
  @Autowired
  private OAuthLogoutSuccessHandler logoutSuccessHandler;

  @Bean
  @Primary
  public SafUserInfoTokenService userInfoTokenServices() {
    return new SafUserInfoTokenService(safService, sso.getUserInfoUri(), sso.getClientId());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //  /authn/validate should be for backend only!
    http.authorizeRequests().antMatchers("/authn/validate*/**", "/templates/**", "/manage/**").permitAll()
        .and()
          .logout()
            .logoutUrl("/authn/logout").permitAll()
            .addLogoutHandler(tokenRevocationLogoutHandler)
            .logoutSuccessHandler(logoutSuccessHandler)
        .and()
          .addFilterBefore(loginServiceValidatorFilter,
            AbstractPreAuthenticatedProcessingFilter.class).csrf().disable();

    super.configure(http);
  }

  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }

  @Bean
  public SecurityContextLogoutHandler logoutHandler() {
    return new SecurityContextLogoutHandler();
  }

  @Bean
  public OAuthLogoutSuccessHandler logoutSuccessHandler() {
    return new OAuthLogoutSuccessHandler();
  }

}
