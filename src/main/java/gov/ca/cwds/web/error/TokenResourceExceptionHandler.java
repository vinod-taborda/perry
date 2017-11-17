package gov.ca.cwds.web.error;

import gov.ca.cwds.rest.api.TokenResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by TPT2 on 11/15/2017.
 */
@ControllerAdvice(assignableTypes = TokenResource.class)
public class TokenResourceExceptionHandler {

  @ExceptionHandler(value = {Exception.class})
  protected Object handleException(Exception ex, WebRequest request) {
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED.getReasonPhrase(), HttpStatus.UNAUTHORIZED);
  }
}