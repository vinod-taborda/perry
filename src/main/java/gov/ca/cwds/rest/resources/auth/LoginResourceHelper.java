package gov.ca.cwds.rest.resources.auth;

import gov.ca.cwds.rest.views.SimpleAccountLoginView;
import io.dropwizard.views.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.apache.shiro.subject.Subject;

/**
 * A helper class for {@link LoginResource} to delegate implementation to.
 * 
 * @author CWDS API Team
 */
public interface LoginResourceHelper {

  /**
   * @param request The request
   * @param response The response
   * @param callback The url to return the user to after a successful login
   * 
   * @return In implementations where login form is hosted on Perry, a view representing the login
   *         form page is returned.
   */
  public View login(HttpServletRequest request, HttpServletResponse response, String callback);


  /**
   * Implementations which do not federate out login will need to implement this method for posting
   * login details.
   * 
   * @param request The request
   * @param response The response
   * @param username The username
   * @param password The password
   * @param callback The callback url
   * 
   * @return If login is not delegated out and the user fails authentication then the login view is
   *         presented with an appropriate message. If login is not delegated out and the user
   *         succeeds authentication then the user is forwarded to the callback. If login is
   *         delegated out, then implementations should throw a NotImplemented Exception.
   */
  public SimpleAccountLoginView loginPost(HttpServletRequest request, HttpServletResponse response,
      String username, String password, String callback);


  /**
   * Get the suject associated with the given token
   * 
   * @param token The token to search for suject on
   * @return The subject associated with the token, null if token invalid or no subject associated.
   */
  public Subject subjectForToken(String token);

  // /**
  // * Validates the passed in token.
  // *
  // * @param token
  // * @return If token valid, 200 response with principal returned, other 401 reponse returned.
  // */
  // public Response validateToken(String token);

  /**
   * Callback endpoint for the OAuth provided to communicate back on.
   * 
   * @param request The request
   * @param response The response
   * @param code The code
   * @param state The state
   * 
   * @return The response
   */
  public Response callback(HttpServletRequest request, HttpServletResponse response, String code,
      String state);
}
