package gov.ca.cwds.config;

import gov.ca.cwds.data.reissue.TokenRepository;
import gov.ca.cwds.data.reissue.model.PerryTokenEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by TPT2 on 10/24/2017.
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "tokenEntityManagerFactory", transactionManagerRef = "tokenTransactionManager", basePackageClasses = TokenRepository.class)
@EntityScan(basePackageClasses = PerryTokenEntity.class)
public class TokenServiceConfiguration {

  @Bean
  @ConfigurationProperties("perry.tokenStore.datasource")
  public DataSourceProperties tokenStoreDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties("perry.tokenStore.datasource")
  public DataSource tokenDataSource() {
    return tokenStoreDataSourceProperties().initializeDataSourceBuilder().build();
  }

  @Bean
  @ConfigurationProperties("perry.tokenStore.jpa")
  public HibernateJpaVendorAdapter tokenJpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  @ConfigurationProperties("perry.tokenStore.jpa")
  public JpaProperties tokenJpaProperties() {
    return new JpaProperties();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean tokenEntityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(tokenDataSource());
    em.setJpaPropertyMap(tokenJpaProperties().getHibernateProperties(tokenDataSource()));
    em.setPackagesToScan("gov.ca.cwds.data.reissue.model");
    em.setPersistenceUnitName("token");
    em.setJpaVendorAdapter(tokenJpaVendorAdapter());
    return em;
  }

  @Bean
  public PlatformTransactionManager tokenTransactionManager() {
    JpaTransactionManager transactionManager
            = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(
            tokenEntityManagerFactory().getObject());
    return transactionManager;
  }
}