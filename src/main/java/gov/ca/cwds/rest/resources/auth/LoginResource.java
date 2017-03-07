package gov.ca.cwds.rest.resources.auth;

import static gov.ca.cwds.rest.core.Api.RESOURCE_USER_AUTHENTICATION;

import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private static final Logger LOGGER = LoggerFactory.getLogger(LoginResource.class);

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
  @ApiOperation(
      value = "Login. Applications should direct users to this endpoint for login.  When authentication complete, user will be redirected back to callback with auth 'token' as a query parameter",
      code = 200)
  public View loginGet(@Context final HttpServletRequest request,
      @NotNull @Context final HttpServletResponse response,
      @ApiParam(required = true, name = "callback",
          value = "URL to send the user back to after authentication") @QueryParam("callback") @NotNull String callback) {
    return loginResourceHelper.login(request, response, callback);
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
  @ApiOperation(value = "Validate an authentication token", code = 200)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "authorized"),
      @ApiResponse(code = 401, message = "Unauthorized")})
  public Response validateToken(@NotNull @ApiParam(required = true, name = "token",
      value = "The token to validate") @QueryParam("token") String token) {
    Subject subject = loginResourceHelper.subjectForToken(token);
    if (subject != null) {
      return Response.status(Status.OK).entity(subject.getPrincipal()).build();
    } else {
      LOGGER.info("Invalid token : {}", token);
      return Response.status(Status.UNAUTHORIZED).entity("Unauthorized").build();
    }
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
