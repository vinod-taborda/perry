package gov.ca.cwds.rest.resources.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import gov.ca.cwds.rest.views.SimpleAccountLoginView;
import io.dropwizard.views.View;

/**
 * A helper class for {@link LoginResource} to delegate implementation to.
 * 
 * @author CWDS API Team
 */
public interface LoginResourceHelper {

  public View loginPage(String callback);

  public SimpleAccountLoginView loginPost(HttpServletRequest request, HttpServletResponse response,
      String username, String password, String callback);

  public Response validateToken(String token);

  public Response callback(HttpServletRequest request, HttpServletResponse response, String code,
      String state);
}
