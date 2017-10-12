package gov.ca.cwds.security;

/**
 * Created by dmitry.rudenko on 7/6/2017.
 */
public class AuthenticationException extends RuntimeException {
  public AuthenticationException(String message) {
    super(message);
  }

  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}
