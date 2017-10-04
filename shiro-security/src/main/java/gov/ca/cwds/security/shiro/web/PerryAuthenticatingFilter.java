package gov.ca.cwds.security.shiro.web;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gov.ca.cwds.security.shiro.PerryShiroToken;
import gov.ca.cwds.security.shiro.utils.Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;


public class PerryAuthenticatingFilter extends AuthenticatingFilter {
  private static final Logger LOGGER = LoggerFactory.getLogger(PerryAuthenticatingFilter.class);
  private static final String PARAM_TOKEN = "token";
  private static final String HEADER_AUTHORIZATION = "Authorization";


  /**
   * Creates a token based on the request/response pair. For Perry authentication, the token is
   * pulled from the query string, with the parameter name `token`.
   *
   * @param request The servlet request
   * @param response The servlet response
   * @return the authentication token
   */
  @Override
  @SuppressFBWarnings("CRLF_INJECTION_LOGS")// Utils.replaceCRLF usage!
  protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String tokenFromRequest = getToken(httpServletRequest);
    if(isNullOrEmpty(tokenFromRequest)) {
      LOGGER.warn("Authorization Token must be passed as request parameter (`{}`) or in the (`{}`) header", PARAM_TOKEN, HEADER_AUTHORIZATION);
      throw new AuthenticationException("Authentication failed");
    }
    PerryShiroToken token = new PerryShiroToken(tokenFromRequest);

    LOGGER.debug("Created a PerryShiroToken Token for: {}.", Utils.replaceCRLF(tokenFromRequest));
    return token;
  }

  /**
   * If a user is not authenticated, this method will be called, and decide if we should attempt to
   * authenticate them.
   * <p>
   * If they have a `token`, confirm the token is valid. If they don't, respond with a 401.
   * </p>
   *
   * @param request The servlet request
   * @param response The servlet response
   * @return <code>true</code> if the request should continue to be processed; false if the subclass
   *         will handle/render the response directly.
   * @throws Exception if there is an error processing the request.
   */
  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String token = getToken(httpServletRequest);

    if (token == null) {
      LOGGER.debug("No token - Denying Access");
      WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided");
    }

    LOGGER.debug("Attempting a login.");
    return executeLogin(request, response);
  }

  private String getToken(HttpServletRequest httpServletRequest) {
    String token = httpServletRequest.getParameter(PARAM_TOKEN);
    if (isNullOrEmpty(token)) {
      token = httpServletRequest.getHeader(HEADER_AUTHORIZATION);
    }
    return token;
  }

  @Override
  protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
      ServletRequest request, ServletResponse response) {
    LOGGER.debug("Invalid Token - Denying Access");
    try {
      WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not valid");
      return false;
    } catch (IOException e1) {
      LOGGER.error("Unable to send 401 for token: {}", ((PerryShiroToken) token).getToken());
      throw new AuthenticationException(MessageFormat.format("Unable to send 401 for token: {0}",
          ((PerryShiroToken) token).getToken()), e1);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.apache.shiro.web.filter.authc.AuthenticatingFilter#onLoginSuccess(org.apache.shiro.authc.
   * AuthenticationToken, org.apache.shiro.subject.Subject, javax.servlet.ServletRequest,
   * javax.servlet.ServletResponse)
   */
  @Override
  protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
      ServletRequest request, ServletResponse response) throws Exception {
    Subject currentUser = SecurityUtils.getSubject();
    // we should never get here. we don't have sessions on APIs
    String userName = currentUser.getPrincipal().toString();
    LOGGER.warn("Existing session when no sessions should be allowed, {}", userName);

    LOGGER.info(userName);
    return super.onLoginSuccess(token, subject, request, response);
  }

  private boolean isNullOrEmpty(String value) {
    return value == null || value.isEmpty();
  }
}
