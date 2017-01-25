package gov.ca.cwds.rest.resources.auth;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.rest.SimpleAccountLoginConfiguration;
import gov.ca.cwds.rest.api.ApiException;
import gov.ca.cwds.rest.views.SimpleAccountLoginView;

/**
 * A {@link LoginResourceHelper} implementation which uses a simple Shiro IniRealm for a user store.
 * 
 * @author CWDS API Team
 */
public class SimpleAccountLoginResourceHelper implements LoginResourceHelper {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(SimpleAccountLoginResourceHelper.class);

  private SimpleAccountLoginConfiguration simpleAccountLoginConfiguration;

  private static final BidiMap tokensToSubjects = new DualHashBidiMap();

  /**
   * Constructor
   * 
   * @param simpleAccountLoginConfiguration The configuration
   */
  @Inject
  public SimpleAccountLoginResourceHelper(
      SimpleAccountLoginConfiguration simpleAccountLoginConfiguration) {
    this.simpleAccountLoginConfiguration = simpleAccountLoginConfiguration;
  }

  @Override
  public Response validateToken(String token) {
    Subject subject = (Subject) tokensToSubjects.get(token);
    if (subject != null && subject.isAuthenticated()) {
      return Response.status(Status.OK).entity(subject.getPrincipal()).build();
    } else {
      LOGGER.info("Invalid token : {}", token);
      return Response.status(Status.UNAUTHORIZED).entity("Unauthorized").build();
    }
  }

  @Override
  public SimpleAccountLoginView loginGet(HttpServletRequest request, HttpServletResponse response,
      String callback) {
    Subject subject = SecurityUtils.getSubject();
    if (subject.isAuthenticated()) {
      try {
        response.sendRedirect(callback);
        return null;
      } catch (IOException e) {
        throw new ApiException(MessageFormat.format("Unable to redirect to callback {0}", callback),
            e);
      }
    }
    return new SimpleAccountLoginView(simpleAccountLoginConfiguration, null, callback);
  }

  @Override
  public Response callback(HttpServletRequest request, HttpServletResponse response, String code,
      String state) {
    return Response.status(Status.NOT_IMPLEMENTED).build();
  }

  @Override
  public SimpleAccountLoginView loginPost(HttpServletRequest request, HttpServletResponse response,
      String username, String password, String callback) {

    Subject subject = SecurityUtils.getSubject();
    removeTokenIfExists(subject);
    AuthenticationToken authenticationToken = new UsernamePasswordToken(username, password);

    try {
      subject.login(authenticationToken);
      if (subject.isAuthenticated()) {
        String token = token(request);
        saveToken(token, subject);
        response.sendRedirect(MessageFormat.format("{0}?token={1}", callback, token));
        return null;
      } else {
        return new SimpleAccountLoginView(simpleAccountLoginConfiguration,
            "Invalid login credentials", callback);
      }
    } catch (UnknownAccountException e) {
      LOGGER.warn("Unknown account login attempt : {}", username);
      return new SimpleAccountLoginView(simpleAccountLoginConfiguration,
          "Invalid login credentials", callback);
    } catch (IncorrectCredentialsException e) {
      LOGGER.warn("Invalid credentials attempt : {}", username);
      return new SimpleAccountLoginView(simpleAccountLoginConfiguration,
          "Invalid login credentials", callback);
    } catch (IOException e) {
      throw new ApiException(MessageFormat.format("Unable to redirect to callback '{0}'", callback),
          e);
    }
  }

  private String token(HttpServletRequest request) {
    return request.getSession().getId();
  }

  private void saveToken(String token, Subject subject) {
    tokensToSubjects.put(token, subject);
  }

  private void removeTokenIfExists(Subject subject) {
    if (subject.isAuthenticated()) {
      String token = (String) tokensToSubjects.getKey(subject);
      if (token != null) {
        tokensToSubjects.remove(token);
      }
    }
  }

}
