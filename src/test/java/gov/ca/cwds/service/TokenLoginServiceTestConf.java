package gov.ca.cwds.service;

import gov.ca.cwds.PerryProperties;
import gov.ca.cwds.config.OAuthConfiguration.ClientProperties;
import gov.ca.cwds.data.auth.AssignmentUnitDao;
import gov.ca.cwds.data.auth.CwsOfficeDao;
import gov.ca.cwds.data.auth.StaffAuthorityPrivilegeDao;
import gov.ca.cwds.data.auth.StaffPersonDao;
import gov.ca.cwds.data.auth.StaffUnitAuthorityDao;
import gov.ca.cwds.data.auth.UserIdDao;
import gov.ca.cwds.data.persistence.auth.UserId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.web.client.RestTemplate;

/**
 * @author CWDS CALS API Team
 */
@Configuration
@ComponentScan(basePackages = {"gov.ca.cwds.service"})
@EnableConfigurationProperties(ClientProperties.class)
@Profile({"UnitTest"})
public class TokenLoginServiceTestConf {

  public static final String TOKEN = "token";
  public static final String TOKEN_EXPIRED = "token_expired";
  public static final String TOKEN2 = "token2";

  @Bean
  public OAuth2ClientContext oauth2ClientContext(){
    DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
    return clientContext;
  }

  @Bean
  public RestTemplate client() {
    return null;
  }

  @Bean
  ResourceServerProperties properties() {
    return null;
  }

  @Bean
  OAuth2RestTemplate oAuth2RestTemplate() {
    return null;
  }

  @Bean
  public ClientProperties clientProperties() {
    ClientProperties clientProperties = new ClientProperties();
    clientProperties.setAccessTokenUri("");
    return clientProperties;
  }

  @Bean
  public SAFService safService() {
    return new SAFService(){
      @Override
      public Map getUserInfo(String accessToken) {
        HashMap<Object, Object> map = new HashMap<>();

        if (TOKEN.equals(accessToken)) {
          map.put("userId", "userId");
        }

        if (TOKEN_EXPIRED.equals(accessToken)) {
          map.put("userId", "userId1");
        }

        if (TOKEN2.equals(accessToken)) {
          map.put("userId", "userId2");
        }

        return map;
      }

      @Override
      public Map validate(String token) {
        HashMap map = new HashMap();
        if (token.equals(TOKEN) || token.equals(TOKEN2)) {
          map.put("active","true");
        } else if (token.equals(TOKEN_EXPIRED)) {
          map.put("active","false");
        } else {
          throw new IllegalArgumentException("");
        }
        return map;
      }
    };

  }

  @Bean
  public TokenStore tokenStore() {
    InMemoryTokenStore inMemoryTokenStore = new InMemoryTokenStore();
    inMemoryTokenStore.setFlushInterval(1);
    return inMemoryTokenStore;
  }

  @Bean
  public TokenServiceImpl tokenService() {
    return new TokenServiceImpl();
  }

  @Bean
  public IdentityMappingService identityMappingService() {
    return new IdentityMappingService();
  }
  
  @Bean
  public TokenLoginService loginService() {
    return new TokenLoginService();
  }

  @Bean
  public UserIdDao userIdDao() {
    return new UserIdDao() {
      @Override
      public List<UserId> findActiveByLogonId(String logonId) {
        return new ArrayList<>();
      }

      @Override
      public UserId findOne(String s) {
        UserId userId = new UserId();
        return userId;
      }

      @Override
      public Iterable<UserId> findAll() {
        return null;
      }
    };
  }

  @Bean
  public StaffAuthorityPrivilegeDao staffAuthorityPrivilegeDao() {
    return null;
  }

  @Bean
  public StaffUnitAuthorityDao staffUnitAuthorityDao() {
    return null;
  }

  @Bean
  public CwsOfficeDao cwsOfficeDao() {
    return null;
  }

  @Bean
  public AssignmentUnitDao assignmentUnitDao() {
    return null;
  }

  @Bean
  public StaffPersonDao staffPersonDao() {
    return null;
  }

  @Bean
  public PerryProperties perryProperties() {
    return new PerryProperties();
  }
}
