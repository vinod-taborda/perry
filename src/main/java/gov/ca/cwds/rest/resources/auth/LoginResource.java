package gov.ca.cwds.rest.resources.auth;

import static gov.ca.cwds.rest.core.Api.RESOURCE_USER_AUTHENTICATION;

import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import gov.ca.cwds.rest.views.SimpleAccountLoginView;
import io.dropwizard.views.View;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Resource handling authn type functionality for Perry.
 * 
 * @author CWDS API Team
 */
@Api(value = RESOURCE_USER_AUTHENTICATION, tags = {RESOURCE_USER_AUTHENTICATION})
@Path(value = RESOURCE_USER_AUTHENTICATION)
public class LoginResource {
  private LoginResourceHelper loginResourceHelper;

  /**
   * Constructor
   * 
   * @param loginResourceHelper The helper
   */
  @Inject
  public LoginResource(LoginResourceHelper loginResourceHelper) {
    super();
    this.loginResourceHelper = loginResourceHelper;
  }

  @GET
  @Path("/login")
  public View loginGet(@Context final HttpServletRequest request,
      @Context final HttpServletResponse response, @QueryParam("callback") String callback) {
    if (StringUtils.isEmpty(callback)) {
      throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
          .entity("callback parameter is mandatory").build());
    }
    return loginResourceHelper.loginGet(request, response, callback);
  }

  @POST
  @Path("/login")
  @ApiOperation(hidden = true, value = "perform username/password login")
  public SimpleAccountLoginView loginPost(@Context final HttpServletRequest request,
      @Context final HttpServletResponse response, @FormParam(value = "username") String username,
      @FormParam(value = "password") String password,

      @FormParam(value = "callback") String callback) {
    if (StringUtils.isEmpty(callback)) {
      throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
          .entity("callback parameter is mandatory").build());
    }
    return loginResourceHelper.loginPost(request, response, username, password, callback);
  }

  @GET
  @Path("/validate")
  @ApiOperation(value = "Validate a token", code = 200)
  @ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized")})
  public Response validateToken(@ApiParam(required = true, name = "token",
      value = "the token") @QueryParam("token") String token) {
    return loginResourceHelper.validateToken(token);
  }

  @GET
  @Path("/callback")
  @ApiOperation(hidden = true, value = "callback for OAuth provider")
  public Response callback(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @QueryParam(value = "code") String code,
      @QueryParam(value = "state") String state) {
    return loginResourceHelper.callback(request, response, code, state);
  }
}
