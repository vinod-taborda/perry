package gov.ca.cwds.web.error;

import gov.ca.cwds.rest.api.LoginResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import static gov.ca.cwds.config.Constants.ERROR_CONTROLLER;
import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * Created by TPT2 on 11/15/2017.
 */
@ControllerAdvice(assignableTypes = LoginResource.class)
public class LoginResourceExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(LoginResourceExceptionHandler.class);

  @ExceptionHandler(value = {Exception.class})
  protected ModelAndView handleException(Exception ex, WebRequest request) {
    logger.error(ex.getMessage(), ex);
    ModelAndView modelAndView = new ModelAndView(ERROR_CONTROLLER);
    request.setAttribute(ERROR_EXCEPTION, ex, SCOPE_REQUEST);
    return modelAndView;
  }
}