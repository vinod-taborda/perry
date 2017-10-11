package gov.ca.cwds.security.module;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import gov.ca.cwds.security.AuthenticationException;
import gov.ca.cwds.security.authorizer.Authorizer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dmitry.rudenko on 9/22/2017.
 */
public class SecurityModule extends AbstractModule {
  private Map<String, Class<? extends Authorizer>> authorizers = new HashMap<>();
  private static InjectorProvider injectorProvider;

  public SecurityModule(InjectorProvider injector) {
    injectorProvider = injector;
  }

  public static Injector injector() {
    if(injectorProvider == null) {
      throw new AuthenticationException("Security Module is not installed!");
    }
    return injectorProvider.getInjector();
  }

  @Override
  protected void configure() {
    authorizers.forEach((name, authorizerClass) -> bind(Authorizer.class)
            .annotatedWith(Names.named(name))
            .to(authorizerClass));
    bindInterceptor(Matchers.inSubpackage("gov.ca.cwds"), SecuredMethodMatcher.hasAuthorizeAnnotation(), new AbacMethodInterceptor());
  }

  public SecurityModule addAuthorizer(String permission, Class<? extends Authorizer> clazz) {
    authorizers.put(permission, clazz);
    return this;
  }
}
