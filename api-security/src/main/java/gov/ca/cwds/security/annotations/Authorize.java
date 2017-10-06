package gov.ca.cwds.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dmitry.rudenko on 9/25/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface Authorize {
  String[] value();
}
