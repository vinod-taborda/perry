package gov.ca.cwds.web;

import gov.ca.cwds.PerryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created by TPT2 on 1/5/2018.
 */
@Controller
public class TargetUrlController {
  @Autowired
  private PerryProperties properties;

  @GetMapping("/")
  public RedirectView handle() {
    return new RedirectView(properties.getTargetUrl());
  }
}
