package gov.ca.cwds.web;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by dmitry.rudenko on 9/14/2017.
 */
@Controller("error")
public class PerryErrorController implements ErrorController {
  private static final String PATH = "/error";


  @RequestMapping("/error")
  @ExceptionHandler(Exception.class)
  public String error(HttpServletResponse response, Model model) {
    return "error";
  }

  @Override
  public String getErrorPath() {
    return PATH;
  }
}
