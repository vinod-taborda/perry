package gov.ca.cwds.security.module;

import com.google.inject.matcher.AbstractMatcher;
import gov.ca.cwds.security.annotations.Authorize;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

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
            || hasAnnotatedPrameters(method);
  }

  private boolean hasAnnotatedPrameters(Method method) {
    for (AnnotatedType annotatedType : method.getAnnotatedParameterTypes()) {
      if (annotatedType.isAnnotationPresent(Authorize.class)) {
        return true;
      }
    }
    return true;
  }
}
