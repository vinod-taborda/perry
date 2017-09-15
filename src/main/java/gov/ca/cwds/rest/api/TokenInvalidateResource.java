package gov.ca.cwds.rest.api;

import gov.ca.cwds.service.TokenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CWDS CALS API Team
 */
@RestController()
public class TokenInvalidateResource {

  @Autowired
  TokenService tokenService;

  @POST
  @RequestMapping("/authn/invalidate")
  @Transactional
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

}
