package gov.ca.cwds.config;

import gov.ca.cwds.PerryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * username format: user:role1,role2
 * password can by any
 */
@Profile("dev")
@Component
public class DevAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  PerryProperties perryProperties;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    tryAuthenticate(authentication);
    String userName = authentication.getName();
    return new UsernamePasswordAuthenticationToken(
            userName, "N/A", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(
            UsernamePasswordAuthenticationToken.class);
  }

  private void tryAuthenticate(Authentication authentication) throws AuthenticationException {
    if(perryProperties.getUsers() != null && !perryProperties.getUsers().isEmpty()) {
      String user = authentication.getName();
      String password = authentication.getCredentials().toString();
      if(!password.equals(perryProperties.getUsers().get(user))){
        throw new BadCredentialsException("User authentication failed");
      }
    }
  }
}