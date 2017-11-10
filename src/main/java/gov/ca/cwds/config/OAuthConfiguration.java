package gov.ca.cwds.config;

import gov.ca.cwds.config.OAuthConfiguration.ClientProperties;
import gov.ca.cwds.service.OauthLogoutHandler;
import gov.ca.cwds.service.SAFService;
import gov.ca.cwds.service.oauth.SafUserInfoTokenService;
import gov.ca.cwds.web.PerrySAFLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * Created by dmitry.rudenko on 5/23/2017.
 */
@Profile("prod")
@EnableOAuth2Sso
@Configuration
@EnableConfigurationProperties(ClientProperties.class)
public class OAuthConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private ResourceServerProperties sso;
  @Autowired
  private ClientProperties clientProperties;
  @Autowired
  private SAFService safService;
  @Autowired
  private LoginServiceValidatorFilter loginServiceValidatorFilter;
  @Autowired
  private OauthLogoutHandler tokenRevocationLogoutHandler;
  @Autowired
  private PerrySAFLogoutSuccessHandler logoutSuccessHandler;


  @Bean
  @Primary
  public SafUserInfoTokenService userInfoTokenServices() {
    return new SafUserInfoTokenService(safService, sso.getUserInfoUri(), sso.getClientId());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //  /authn/validate should be for backend only!
    http.authorizeRequests().antMatchers("/authn/validate*/**", "/authn/invalidate*/**", "/templates/**", "/manage/**").permitAll()
        .antMatchers("/perry-login.html").denyAll()
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
  public OAuth2RestTemplate oAuth2RestTemplate() {
    ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
    resource.setAccessTokenUri(clientProperties.getAccessTokenUri());
    resource.setClientId(sso.getClientId());
    resource.setClientSecret(sso.getClientSecret());
    resource.setAuthenticationScheme(clientProperties.getAuthenticationScheme());
    resource.setClientAuthenticationScheme(clientProperties.getClientAuthenticationScheme());
    return  new OAuth2RestTemplate(resource);
  }

  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }

  @ConfigurationProperties(prefix = "security.oauth2.client")
  public static class ClientProperties{

    private String accessTokenUri;

    private AuthenticationScheme authenticationScheme;
    private AuthenticationScheme clientAuthenticationScheme;

    public String getAccessTokenUri() {
      return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
      this.accessTokenUri = accessTokenUri;
    }

    public AuthenticationScheme getAuthenticationScheme() {
      return authenticationScheme;
    }

    public void setAuthenticationScheme(AuthenticationScheme authenticationScheme) {
      this.authenticationScheme = authenticationScheme;
    }

    public AuthenticationScheme getClientAuthenticationScheme() {
      return clientAuthenticationScheme;
    }

    public void setClientAuthenticationScheme(AuthenticationScheme clientAuthenticationScheme) {
      this.clientAuthenticationScheme = clientAuthenticationScheme;
    }
  }

}
