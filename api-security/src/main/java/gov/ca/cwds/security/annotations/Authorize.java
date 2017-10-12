package gov.ca.cwds.security.annotations;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dmitry.rudenko on 9/25/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Authorize {
  String[] value();
}
