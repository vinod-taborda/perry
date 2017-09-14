package gov.ca.cwds.rest.api;

import gov.ca.cwds.service.LoginService;
import gov.ca.cwds.service.OauthLoginService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@RestController
public class LoginResource {

  @Autowired
  LoginService loginService;

  @GET
  @RequestMapping("/authn/login")
  @Transactional
  @ApiOperation(
      value = "Login. Applications should direct users to this endpoint for login.  When authentication complete, user will be redirected back to callback with auth 'token' as a query parameter",
      code = 200)
  public void login(@NotNull @Context final HttpServletResponse response,
      @ApiParam(required = true, name = "callback",
          value = "URL to send the user back to after authentication") @RequestParam("callback") @NotNull String callback,
      @ApiParam(name = "sp_id",
          value = "Service provider id") @RequestParam(name = "sp_id", required = false) String spId)
      throws Exception {
    String jwtToken = loginService.login(spId);
    response.sendRedirect(callback + "?token=" + jwtToken);
  }

  //back-end only!
  @GET
  @RequestMapping(value = "/authn/validate", produces = "application/json")
  @ApiOperation(value = "Validate an authentication token", code = 200)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "authorized"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  public String validateToken(@Context final HttpServletResponse response,
      @NotNull @ApiParam(required = true, name = "token",
          value = "The token to validate") @RequestParam("token") String token) {
    try {
      return loginService.validate(token);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return "Unauthorized";
    }
  }


  /**
   *
   *  Support backward compatibility
   *
   * @param response
   * @param callback
   * @param spId
   * @throws Exception
   */
  @GET
  @RequestMapping("/authn/login/v2")
  @Transactional
  @ApiOperation(
      value = "Login. Applications should direct users to this endpoint for login.  When authentication complete, user will be redirected back to callback with auth 'token' as a query parameter (endpoint used for backward compatibility)",
      code = 200)
  public void loginV2(@NotNull @Context final HttpServletResponse response,
      @ApiParam(required = true, name = "callback",
          value = "URL to send the user back to after authentication") @RequestParam("callback") @NotNull String callback,
      @ApiParam(name = "sp_id",
          value = "Service provider id") @RequestParam(name = "sp_id", required = false) String spId)
      throws Exception {

    if (!(loginService instanceof OauthLoginService)) {
      throw new UnsupportedOperationException();
    }

    String jwtToken = ((OauthLoginService) loginService).loginV2(spId);
    response.sendRedirect(callback + "?token=" + jwtToken);
  }

  /**
   *
   * Support backward compatibility
   *
   * @param response
   * @param token
   * @return
   */
  //back-end only!
  @GET
  @RequestMapping(value = "/authn/validate/v2", produces = "application/json")
  @ApiOperation(value = "Validate an authentication token (endpoint used for backward compatibility) ", code = 200)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "authorized"),
      @ApiResponse(code = 401, message = "Unauthorized")
  })
  public String validateTokenV2(@Context final HttpServletResponse response,
      @NotNull @ApiParam(required = true, name = "token",
          value = "The token to validate") @RequestParam("token") String token) {

    if (!(loginService instanceof OauthLoginService)) {
      throw new UnsupportedOperationException();
    }

    try {
      String validationResponse = ((OauthLoginService) loginService).validateV2(token);
      response.setStatus(HttpServletResponse.SC_OK);
      return validationResponse;
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return "Unauthorized";
    }
  }

}
