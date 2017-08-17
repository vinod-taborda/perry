package gov.ca.cwds.config;

import gov.ca.cwds.PerryProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

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
    try {
      if (!StringUtils.isEmpty(perryProperties.getUsers())) {
        String user = authentication.getName();
        String password = authentication.getCredentials().toString();
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Paths.get(perryProperties.getUsers())));
        if (!properties.containsKey(user) || !properties.getProperty(user).equals(password)) {
          throw new BadCredentialsException("Authentication failed");
        }
      }
    } catch (IOException e) {
      throw new AuthenticationServiceException("Can't read users", e);
    }
  }
}