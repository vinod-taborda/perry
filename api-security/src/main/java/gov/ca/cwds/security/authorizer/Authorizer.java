package gov.ca.cwds.security.authorizer;

/**
 * Created by dmitry.rudenko on 9/25/2017.
 */
public interface Authorizer {
  boolean check(String id);
}
