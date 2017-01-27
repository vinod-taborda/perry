package gov.ca.cwds.rest.resources.auth;

import static gov.ca.cwds.rest.core.Api.RESOURCE_USER_AUTHORIZATION;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.eclipse.jetty.server.Authentication.User;

import com.google.inject.Inject;

import gov.ca.cwds.inject.UserAuthorizationServiceBackedResource;
import gov.ca.cwds.rest.api.domain.auth.UserAuthorization;
import gov.ca.cwds.rest.resources.ResourceDelegate;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * A resource providing a RESTful interface for {@link UserAuthorization}. It delegates functions to
 * {@link ResourceDelegate}. It decorates the {@link ResourceDelegate} not in functionality but
 * with @see <a href= "https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X">Swagger
 * Annotations</a> and
 * <a href="https://jersey.java.net/documentation/latest/user-guide.html#jaxrs-resources">Jersey
 * Annotations</a>
 * 
 * @author CWDS API Team
 */
@Api(hidden = true, value = RESOURCE_USER_AUTHORIZATION, tags = {RESOURCE_USER_AUTHORIZATION})
@Path(value = RESOURCE_USER_AUTHORIZATION)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAuthorizationResource {

  private ResourceDelegate resourceDelegate;

  /**
   * Constructor
   * 
   * @param resourceDelegate The resourceDelegate to delegate to.
   */
  @Inject
  public UserAuthorizationResource(
      @UserAuthorizationServiceBackedResource ResourceDelegate resourceDelegate) {
    this.resourceDelegate = resourceDelegate;
  }

  /**
   * Finds an user authorization by id.
   * 
   * @param id the id
   * 
   * @return the response
   */
  @UnitOfWork(value = "cms")
  @GET
  @Path("/{id}")
  @ApiResponses(value = {@ApiResponse(code = 404, message = "Not found"),
      @ApiResponse(code = 406, message = "Accept Header not supported")})
  @ApiOperation(value = "Find User Authorization by id", response = UserAuthorization.class,
      code = 200)
  public Response get(@PathParam("id") @ApiParam(required = true, name = "id",
      value = "The id of the User Authorization to find") String id) {
    return resourceDelegate.get(id);
  }

  /**
   * Not Implemented Delete a user authorization by id.
   * 
   * @param id The id of the {@link User Authorization}
   * 
   * @return {@link Response}
   */
  @UnitOfWork(value = "cms")
  @DELETE
  @Path("/{id}")
  @ApiOperation(hidden = true, value = "Delete UserAuthorization", code = HttpStatus.SC_OK,
      response = Object.class)
  public Response delete(@PathParam("id") @ApiParam(required = true,
      value = "id of UserAuthorization to delete") String id) {
    return Response.status(HttpStatus.SC_NOT_IMPLEMENTED).build();
  }

  /**
   * Not Implemented Create an {@link UserAuthorization}
   * 
   * @param userAuthorization The {@link UserAuthorization}
   * 
   * @return The {@link Response}
   */
  @UnitOfWork(value = "cms")
  @POST
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Unable to process JSON"),
      @ApiResponse(code = 406, message = "Accept Header not supported"),
      @ApiResponse(code = 409, message = "Conflict - already exists"),
      @ApiResponse(code = 422, message = "Unable to validate UserAuthorization")})
  @Consumes(value = MediaType.APPLICATION_JSON)
  @ApiOperation(hidden = true, value = "Create UserAuthorization", code = HttpStatus.SC_CREATED,
      response = UserAuthorization.class)
  public Response create(
      @Valid @ApiParam(hidden = false, required = true) UserAuthorization userAuthorization) {
    return Response.status(HttpStatus.SC_NOT_IMPLEMENTED).build();
  }

  /**
   * Not Implemented Update an {@link UserAuthorization}
   * 
   * @param id the id
   * @param userAuthorization {@link UserAuthorization}
   *
   * @return The {@link Response}
   */
  @UnitOfWork(value = "cms")
  @PUT
  @Path("/{id}")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Unable to process JSON"),
      @ApiResponse(code = 404, message = "not found"),
      @ApiResponse(code = 406, message = "Accept Header not supported"),
      @ApiResponse(code = 422, message = "Unable to validate UserAuthorization")})
  @Consumes(value = MediaType.APPLICATION_JSON)
  @ApiOperation(hidden = true, value = "Update UserAuthorization", code = HttpStatus.SC_NO_CONTENT,
      response = Object.class)
  public Response update(
      @PathParam("id") @ApiParam(required = true, name = "id",
          value = "The id of the UserAuthorization to update") String id,
      @Valid @ApiParam(hidden = false, required = true) UserAuthorization userAuthorization) {
    return Response.status(HttpStatus.SC_NOT_IMPLEMENTED).build();
  }
}
