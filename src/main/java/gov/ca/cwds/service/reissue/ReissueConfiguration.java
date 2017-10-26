package gov.ca.cwds.service.reissue;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;

import javax.sql.DataSource;

/**
 * Created by TPT2 on 10/24/2017.
 */
@Configuration
public class ReissueConfiguration {

  @Bean(name = "tokenStoreDSProperties")
  @ConfigurationProperties("spring.tokenStore")
  public DataSourceProperties tokenStoreDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean(name = "tokenStoreDS")
  @ConfigurationProperties("spring.tokenStore")
  public DataSource tokenStoreDataSource() {
    return tokenStoreDataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean
  public ClientTokenServices clientTokenServices() {
    return new JdbcClientTokenServices(tokenStoreDataSource());
  }

  @Bean
  public AuthorizationCodeServices authorizationCodeServices() {
    return new InMemoryAuthorizationCodeServices();
  }

}
