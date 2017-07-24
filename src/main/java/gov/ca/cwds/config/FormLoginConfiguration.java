package gov.ca.cwds.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Map;

/**
 * Created by dmitry.rudenko on 5/23/2017.
 */
@Profile("dev")
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties()
public class FormLoginConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DevAuthenticationProvider authProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/static/**", "/login*", "/css/**","/images/**", "/dev/**", "/authn/validate*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/static/login.html")
                .defaultSuccessUrl("/static/index.html")
                .loginProcessingUrl("/static/login")
                .failureUrl("/static/login.html?error=true")
                .and()
                .logout().logoutSuccessUrl("/static/login.html").and().csrf().disable();
    }
}