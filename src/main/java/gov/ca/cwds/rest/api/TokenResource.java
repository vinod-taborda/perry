package gov.ca.cwds.rest.api;

import gov.ca.cwds.config.Constants;
import gov.ca.cwds.service.LoginService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@RestController
public class TokenResource {

  private LoginService loginService;

  @GET
  @RequestMapping(value = Constants.TOKEN_SERVICE_URL, produces = "application/json")
  @ApiOperation(value = "Get perry token")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "authorized"),
          @ApiResponse(code = 401, message = "Unauthorized")})
  public String getToken(@NotNull @ApiParam(required = true, name = "accessCode", value = "Access Code to map")
                         @RequestParam("accessCode") String accessCode) {
    return loginService.issueToken(accessCode);
  }

  //back-end only!
  @GET
  @RequestMapping(value = Constants.VALIDATE_SERVICE_URL, produces = "application/json")
  @ApiOperation(value = "Validate an authentication token")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "authorized"),
          @ApiResponse(code = 401, message = "Unauthorized")})
  public String validateToken(@NotNull @ApiParam(required = true, name = "token", value = "The token to validate")
                              @RequestParam("token") String token) {
    return loginService.validate(token);
  }

  @POST
  @RequestMapping("/authn/invalidate")
  @ApiOperation(value = "Invalidate token")
  public String invalidate(@NotNull @ApiParam(required = true, name = "token", value = "The token to invalidate")
                           @RequestParam("token") String token) {
    loginService.invalidate(token);
    return "OK";
  }

  @Autowired
  public void setLoginService(LoginService loginService) {
    this.loginService = loginService;
  }

}
