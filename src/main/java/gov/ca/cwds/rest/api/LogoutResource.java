package gov.ca.cwds.rest.api;

import gov.ca.cwds.service.LogoutService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@Profile("prod")
@RestController
public class LogoutResource {

  @Autowired
  private LogoutService logoutService;

  @GET
  @RequestMapping("/authn/logout/v2")
  @ApiOperation(value = "Logout",  code = 200)
  public void logout(
      @NotNull @Context
        final HttpServletRequest request,
      @NotNull @Context
        final HttpServletResponse response,
      @ApiParam(required = true, name = "callback", value = "URL to send the user back to")
      @RequestParam("callback") @NotNull
        String callback) throws IOException {

    logoutService.logout(request, response);
    response.sendRedirect(callback);
  }

}
