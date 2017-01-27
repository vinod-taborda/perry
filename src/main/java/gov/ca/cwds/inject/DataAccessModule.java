package gov.ca.cwds.inject;

import org.hibernate.SessionFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import gov.ca.cwds.data.auth.StaffAuthorityPrivilegeDao;
import gov.ca.cwds.data.auth.StaffUnitAuthorityDao;
import gov.ca.cwds.data.auth.UserAuthorizationDao;
import gov.ca.cwds.data.auth.UserIdDao;
import gov.ca.cwds.data.persistence.auth.StaffAuthorityPrivilege;
import gov.ca.cwds.data.persistence.auth.StaffUnitAuthority;
import gov.ca.cwds.data.persistence.auth.UserId;
import gov.ca.cwds.rest.ElasticsearchConfiguration;
import gov.ca.cwds.rest.SecurityApiConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;

/**
 * DI (dependency injection) setup for data access objects (DAO).
 * 
 * @author CWDS API Team
 */
public class DataAccessModule extends AbstractModule {
  private final HibernateBundle<SecurityApiConfiguration> cmsHibernateBundle =
      new HibernateBundle<SecurityApiConfiguration>(UserId.class, StaffAuthorityPrivilege.class,
          StaffUnitAuthority.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(SecurityApiConfiguration configuration) {
          return configuration.getCmsDataSourceFactory();
        }

        @Override
        public String name() {
          return "cms";
        }
      };

  public DataAccessModule(Bootstrap<SecurityApiConfiguration> bootstrap) {
    // RDB - data access out of scope for R1. Removing from bootstrap for simplicity
    // bootstrap.addBundle(cmsHibernateBundle);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.google.inject.AbstractModule#configure()
   */
  @Override
  protected void configure() {
    bind(UserAuthorizationDao.class);
    bind(UserIdDao.class);
    bind(StaffAuthorityPrivilegeDao.class);
    bind(StaffUnitAuthorityDao.class);
  }

  @Provides
  @CmsSessionFactory
  SessionFactory cmsSessionFactory() {
    return cmsHibernateBundle.getSessionFactory();
  }

  @Provides
  public ElasticsearchConfiguration elasticSearchConfig(SecurityApiConfiguration apiConfiguration) {
    return apiConfiguration.getElasticsearchConfiguration();
  }

}
