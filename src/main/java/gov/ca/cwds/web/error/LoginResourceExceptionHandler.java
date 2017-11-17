package gov.ca.cwds.web.error;

import gov.ca.cwds.rest.api.LoginResource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * Created by TPT2 on 11/15/2017.
 */
@ControllerAdvice(assignableTypes = LoginResource.class)
public class LoginResourceExceptionHandler {

  @ExceptionHandler(value = {Exception.class})
  protected ModelAndView handleException(Exception ex, WebRequest request) {
    ModelAndView modelAndView = new ModelAndView("error");
    request.setAttribute("e", ex, SCOPE_REQUEST);
    return modelAndView;
  }
}