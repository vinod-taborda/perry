package gov.ca.cwds.security.module;

import com.google.inject.matcher.AbstractMatcher;
import gov.ca.cwds.security.annotations.Authorize;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by dmitry.rudenko on 10/9/2017.
 */
public class SecuredMethodMatcher extends AbstractMatcher<Method> {

  private SecuredMethodMatcher() {

  }

  public static SecuredMethodMatcher hasAuthorizeAnnotation() {
    return new SecuredMethodMatcher();
  }

  @Override
  public boolean matches(Method method) {
    return method.getAnnotation(Authorize.class) != null
            || hasAnnotatedParameters(method);
  }

  private boolean hasAnnotatedParameters(Method method) {
    for (Parameter parameter : method.getParameters()) {
      if (parameter.isAnnotationPresent(Authorize.class)) {
        return true;
      }
    }
    return false;
  }
}
