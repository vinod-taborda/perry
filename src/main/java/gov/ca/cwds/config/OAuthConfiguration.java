package gov.ca.cwds.config;

import gov.ca.cwds.data.auth.AssignmentUnitDao;
import gov.ca.cwds.data.persistence.auth.AssignmentUnit;
import gov.ca.cwds.service.OauthLogoutHandler;
import gov.ca.cwds.service.oauth.SafUserInfoTokenService;
import gov.ca.cwds.web.PerrySAFLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by dmitry.rudenko on 5/23/2017.
 */
@Profile("prod")
@EnableOAuth2Sso
@Configuration
public class OAuthConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private LoginServiceValidatorFilter loginServiceValidatorFilter;
  @Autowired
  private OauthLogoutHandler tokenRevocationLogoutHandler;
  @Autowired
  private PerrySAFLogoutSuccessHandler logoutSuccessHandler;

  @Bean
  @Primary
  @Autowired
  public SafUserInfoTokenService userInfoTokenServices(ResourceServerProperties resourceServerProperties) {
    return new SafUserInfoTokenService(resourceServerProperties);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //  /authn/validate should be for backend only!
    http.authorizeRequests().antMatchers("/authn/validate*/**", "/authn/invalidate*/**", "/templates/**", "/manage/**", "/authn/token*/**").permitAll()
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
}
