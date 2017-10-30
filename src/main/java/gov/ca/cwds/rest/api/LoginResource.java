package gov.ca.cwds.rest.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gov.ca.cwds.config.Constants;
import gov.ca.cwds.service.WhiteList;
import gov.ca.cwds.service.reissue.ReissueLoginService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import java.util.logging.Logger;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@RestController
public class LoginResource {

  private ReissueLoginService loginService;

  private WhiteList whiteList;

  @GET
  @RequestMapping(Constants.LOGIN_SERVICE_URL)
  @ApiOperation(
          value = "Login. Applications should direct users to this endpoint for login.  When authentication complete, user will be redirected back to callback with auth 'token' as a query parameter",
          code = 200)
  @SuppressFBWarnings("UNVALIDATED_REDIRECT")//white list usage right before redirect


  public void login(@NotNull @Context final HttpServletResponse response,
                    @ApiParam(required = true, name = "callback",
                            value = "URL to send the user back to after authentication") @RequestParam(Constants.CALLBACK_PARAM) String callback,
                    @ApiParam(name = "sp_id",
                            value = "Service provider id") @RequestParam(name = "sp_id", required = false) String spId) throws Exception {
    String accessCode = loginService.issueAccessCode(spId);
    whiteList.validate("callback", callback);
    response.sendRedirect(callback + "?accessCode=" + accessCode);
  }

  @GET
  @RequestMapping(value = Constants.TOKEN_SERVICE_URL, produces = "application/json")
  @ApiOperation(value = "Get perry token", code = 200)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "authorized"),
          @ApiResponse(code = 401, message = "Unauthorized")})
  public String getToken(@Context final HttpServletResponse response, @NotNull @ApiParam(required = true, name = "accessCode",
          value = "Access Code to map") @RequestParam("accessCode") String accessCode) {
    try {
      return loginService.issueToken(accessCode);
    } catch (Exception e) {
      Logger.getLogger(LoginResource.class.getName()).info(e.getMessage());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return "Unauthorized";
    }

  }

  //back-end only!
  @GET
  @RequestMapping(value = Constants.VALIDATE_SERVICE_URL, produces = "application/json")
  @ApiOperation(value = "Validate an authentication token", code = 200)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "authorized"),
          @ApiResponse(code = 401, message = "Unauthorized")})
  public String validateToken(@Context final HttpServletResponse response, @NotNull @ApiParam(required = true, name = "token",
          value = "The token to validate") @RequestParam("token") String token) {
    try {
      return loginService.validate(token);
    } catch (Exception e) {
      Logger.getLogger(LoginResource.class.getName()).info(e.getMessage());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return "Unauthorized";
    }

  }

  @POST
  @RequestMapping("/authn/invalidate")
  @ApiOperation(
          value = "Invalidate token",
          code = 200)
  public String invalidate(@NotNull @Context final HttpServletResponse response,
                           @NotNull @ApiParam(required = true, name = "token",
                                   value = "The token to invalidate") @RequestParam("token") String token) {
    loginService.invalidate(token);
    response.setStatus(HttpServletResponse.SC_OK);
    return "OK";
  }

  @Autowired
  public void setLoginService(ReissueLoginService loginService) {
    this.loginService = loginService;
  }

  @Autowired
  public void setWhiteList(WhiteList whiteList) {
    this.whiteList = whiteList;
  }
}
