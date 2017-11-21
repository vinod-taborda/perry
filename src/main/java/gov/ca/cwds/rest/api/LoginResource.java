package gov.ca.cwds.rest.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gov.ca.cwds.config.Constants;
import gov.ca.cwds.service.LoginService;
import gov.ca.cwds.service.WhiteList;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@RestController
public class LoginResource {

  private LoginService loginService;

  private WhiteList whiteList;

  @GET
  @RequestMapping(Constants.LOGIN_SERVICE_URL)
  @Transactional
  @ApiOperation(
          value = "Login. Applications should direct users to this endpoint for login.  When authentication complete, user will be redirected back to callback with auth 'token' as a query parameter",
          code = 200)
  @SuppressFBWarnings("UNVALIDATED_REDIRECT")//white list usage right before redirect
  public void login(@NotNull @Context final HttpServletResponse response,
                    @ApiParam(required = true, name = "callback",
                            value = "URL to send the user back to after authentication") @RequestParam(Constants.CALLBACK_PARAM) String callback,
                    @ApiParam(name = "sp_id",
                            value = "Service provider id") @RequestParam(name = "sp_id", required = false) String spId) throws Exception {
    String jwtToken = loginService.login(spId);
    whiteList.validate("callback", callback);
    response.sendRedirect(callback + "?token=" + jwtToken);
  }

  @Autowired
  public void setLoginService(LoginService loginService) {
    this.loginService = loginService;
  }

  @Autowired
  public void setWhiteList(WhiteList whiteList) {
    this.whiteList = whiteList;
  }
}
