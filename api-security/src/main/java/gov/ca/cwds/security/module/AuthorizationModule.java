package gov.ca.cwds.security.module;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import gov.ca.cwds.security.AuthenticationException;
import gov.ca.cwds.security.authorizer.Authorizer;
import gov.ca.cwds.security.authorizer.BaseAuthorizer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmitry.rudenko on 9/22/2017.
 */
public class AuthorizationModule extends AbstractModule {

  private static Injector injector;

  private final Map<String, Class<? extends BaseAuthorizer>> authorizers = new HashMap<>();
  private Boolean enabled = Boolean.TRUE;

  @Override
  protected void configure() {
    if (!this.enabled) {
      return;
    }

    authorizers.forEach((name, authorizerClass) -> bind(Authorizer.class)
        .annotatedWith(Names.named(name))
        .to(authorizerClass));

    bindInterceptor(
        Matchers.inSubpackage("gov.ca.cwds"),
        SecuredMethodMatcher.hasAuthorizeAnnotation(),
        new AbacMethodInterceptor()
    );
  }

  public AuthorizationModule addAuthorizer(String permission, Class<? extends BaseAuthorizer> clazz) {
    authorizers.put(permission, clazz);
    return this;
  }

  public AuthorizationModule setEnabled(final Boolean enabled) {
    this.enabled = enabled == null || enabled;
    return this;
  }

  public static void setInjector(final Injector injectorInstance) {
    injector = injectorInstance;
  }

  public static Injector injector() {
    if (injector == null) {
      throw new AuthenticationException("Security Module is not installed!");
    }
    return injector;
  }
}
