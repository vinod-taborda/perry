package gov.ca.cwds.service;

/**
 * @author CWDS CALS API Team
 */
public class SAFServiceException extends Exception {

  public SAFServiceException() {
  }

  public SAFServiceException(String message) {
    super(message);
  }

  public SAFServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public SAFServiceException(Throwable cause) {
    super(cause);
  }

  public SAFServiceException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
