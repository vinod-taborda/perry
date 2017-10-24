package gov.ca.cwds.rest.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gov.ca.cwds.config.Constants;
import gov.ca.cwds.service.TokenLoginService;
import gov.ca.cwds.service.TokenService;
import gov.ca.cwds.service.WhiteList;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CWDS CALS API Team
 */
@Profile("prod")
@RestController("LoginResourceV2")
public class LoginResourceV2 {


  private TokenLoginService loginService;
  private WhiteList whiteList;
  private TokenService tokenService;

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
      value = "Login. Applications should direct users to this endpoint for login.  When authentication complete, user will be redirected back to callback with access code as a query parameter (endpoint used for backward compatibility)",
      code = 200)
  @SuppressFBWarnings("UNVALIDATED_REDIRECT")//white list usage right before redirect
  public void loginV2(@NotNull @Context final HttpServletResponse response,
      @ApiParam(required = true, name = "callback",
          value = "URL to send the user back to after authentication") @RequestParam(name = "callback") String callback,
      @ApiParam(name = "sp_id",
          value = "Service provider id") @RequestParam(name = "sp_id", required = false) String spId)
      throws Exception {
    whiteList.validate("callback", callback);
    String accessCode = loginService.login(spId);
    response.sendRedirect(callback + "?access_code=" + accessCode);
  }

  //back-end only!
  @GET
  @RequestMapping(value = Constants.ISSUE_TOKEN_SERVICE_URL, produces = "application/json")
  @ApiOperation(value = "Issue an authentication token", code = 200)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "authorized"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  public String issueToken(@Context final HttpServletResponse response, @NotNull @ApiParam(required = true, name = "accessCode",
      value = "The access code to get token") @RequestParam("accessCode") String accessCode) {
    try {
      return loginService.issueToken(accessCode);
    } catch (Exception e) {
      Logger.getLogger(LoginResource.class.getName()).info(e.getMessage());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return "Unauthorized";
    }

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
    try {
      String validationResponse = loginService.validate(token);
      response.setStatus(HttpServletResponse.SC_OK);
      return validationResponse;
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return "Unauthorized";
    }
  }

  @POST
  @RequestMapping("/authn/invalidate/v2")
  @ApiOperation(
      value = "Invalidate token",
      code = 200)
  public String invalidate(@NotNull @Context final HttpServletResponse response,
      @NotNull @ApiParam(required = true, name = "token",
          value = "The token to invalidate") @RequestParam("token") String token) {
    try{
      tokenService.invalidate(token);
      response.setStatus(HttpServletResponse.SC_OK);
      return "OK";
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      return "Invalid Token";
    }
  }

  @Autowired
  public void setLoginService(TokenLoginService loginService) {
    this.loginService = loginService;
  }

  @Autowired
  public void setWhiteList(WhiteList whiteList) {
    this.whiteList = whiteList;
  }

  @Autowired
  public void setTokenService(TokenService tokenService) {
    this.tokenService = tokenService;
  }
}
