package gov.ca.cwds.security.module;

import com.google.inject.Injector;

/**
 * Created by TPT2 on 10/11/2017.
 */
@FunctionalInterface
public interface InjectorProvider {
  Injector getInjector();
}
