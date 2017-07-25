package gov.ca.cwds.security.jwt;

/**
 * Created by dmitry.rudenko on 7/24/2017.
 */
public class JwtException extends RuntimeException {
  public JwtException(String message, Throwable cause) {
    super(message, cause);
  }

  public JwtException(Throwable cause) {
    super(cause);
  }

  public JwtException(String message) {
    super(message);
  }
}
