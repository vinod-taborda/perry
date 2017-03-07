package gov.ca.cwds.rest.resources.auth.saf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import gov.ca.cwds.rest.SAFConfiguration;
import gov.ca.cwds.rest.api.ApiException;
import gov.ca.cwds.rest.api.domain.PerryUser;
import gov.ca.cwds.rest.resources.auth.LoginResourceHelper;
import gov.ca.cwds.rest.views.SimpleAccountLoginView;
import io.dropwizard.views.View;

public class SafLoginResourceHelper implements LoginResourceHelper {

  /**
   * {@link ObjectMapper}, used to unmarshall JSON Strings from member {@link #sourceJson} into
   * instances of {@link #sourceType}.
   * 
   * <p>
   * This mapper is thread-safe and reusable across multiple threads, yet any configuration made to
   * it, such as ignoring unknown JSON properties, applies to ALL target class types.
   * </p>
   */
  private static final ObjectMapper MAPPER;

  // =========================
  // STATIC INITIALIZATION:
  // =========================

  /**
   * Relax strict constraints regarding unknown JSON properties, since API classes may change over
   * time, and not all classes emit version information in JSON.
   */
  static {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    MAPPER = mapper;
  }

  private static final String UNAUTHORIZED = "Unauthorized";

  private static final String BEARER = "bearer ";

  private static final Logger LOGGER = LoggerFactory.getLogger(SafLoginResourceHelper.class);

  private static final String ENCODING = "UTF-8";

  private String authUrl;
  private String retrieveTokenUrl;
  private String validateTokenUrl;
  private String clientId;
  private String clientSecret;
  private String callbackUrl;
  private String scope;

  private static final String RESPONSE_TYPE = "code";

  /**
   * key = "state" sent to SAF.
   */
  private static final Map<String, SAFAuthBean> pendingAuthByState = new ConcurrentHashMap<>();

  /**
   * key = code from SAF callback.
   */
  private static final Map<String, SAFAuthBean> pendingAuthByCode = new ConcurrentHashMap<>();

  /**
   * key = access token.
   */
  private static final Map<String, SAFAuthBean> authByAccessToken = new ConcurrentHashMap<>();

  /**
   * key = "state"
   */
  private static final Map<String, String> urlByState = new ConcurrentHashMap<>();

  private Client client;

  /**
   * Constructor
   * 
   * @param safConfiguration Configuration class containing the details needed to interact with SAF
   * @param client A jersey client for making http calls to SAF
   */
  @Inject
  public SafLoginResourceHelper(final SAFConfiguration safConfiguration, final Client client) {
    this.client = client;
    this.authUrl = safConfiguration.getBaseUrl() + safConfiguration.getAuthPath();
    this.retrieveTokenUrl = safConfiguration.getBaseUrl() + safConfiguration.getRetrieveTokenPath();
    this.validateTokenUrl = safConfiguration.getBaseUrl() + safConfiguration.getValidateTokenPath();
    this.clientId = safConfiguration.getClientId();
    this.clientSecret = safConfiguration.getClientSecret();
    this.callbackUrl = safConfiguration.getCallbackUrl();
    this.scope = safConfiguration.getScope();
  }

  private void bindUrlToState(String state, String url) {
    urlByState.put(state, url);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.ca.cwds.rest.resources.auth.LoginResourceHelper#login(javax.servlet.http.
   * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
   */
  @Override
  public View login(HttpServletRequest request, HttpServletResponse response, String callback) {

    final String state = state();
    bindUrlToState(state, callback);
    SAFAuthBean auth = new SAFAuthBean();
    auth.setState(state);
    pendingAuthByState.put(state, auth);

    String fullUrl = "";

    try {
      StringBuilder buf = new StringBuilder();
      buf.append(authUrl).append("?client_id=").append(clientId.toUpperCase())
          .append("&response_type=").append(RESPONSE_TYPE).append("&redirect_uri=")
          .append(callbackUrl).append("&scope=").append(scope).append("&state=").append(state);
      fullUrl = buf.toString();
      LOGGER.debug("Redirect for login to {}", fullUrl);
      response.sendRedirect(fullUrl);
    } catch (UnsupportedEncodingException e) {
      LOGGER.error("Unknown Encoding {}", ENCODING);
      throw new ApiException("Unknown Encoding", e);
    } catch (IOException e) {
      throw new ApiException("unable to redirect to the url:" + fullUrl, e);
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.ca.cwds.rest.resources.auth.LoginResourceHelper#loginPost(javax.servlet.http.
   * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String, java.lang.String,
   * java.lang.String)
   */
  @Override
  public SimpleAccountLoginView loginPost(HttpServletRequest request, HttpServletResponse response,
      String username, String password, String callback) {
    throw new NotImplementedException("Post to handle login credentials to Perry not implemented");
  }



  /*
   * (non-Javadoc)
   * 
   * @see gov.ca.cwds.rest.resources.auth.LoginResourceHelper#subjectForToken(java.lang.String)
   */
  @Override
  public Subject subjectForToken(String token) {
    Response response =
        client.target(validateTokenUrl).request().header(HttpHeaders.AUTHORIZATION, BEARER + token)
            .post(Entity.entity(BEARER + token, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

    if (response.getStatus() != (Status.OK.getStatusCode())) {
      LOGGER.info("TOKEN VALIDATION FAILED! " + response);
      return null;
    }

    try {
      PerryUser user = MAPPER.readValue(response.readEntity(String.class), PerryUser.class);
      LOGGER.debug("Subject: {}", user);

      List<Object> principals = Lists.<Object>newArrayList(user.getUsername());
      PrincipalCollection principalCollection = new SimplePrincipalCollection(principals, "saf");

      Subject subject = SecurityUtils.getSubject();
      return new WebDelegatingSubject(principalCollection, true, subject.getSession().getHost(),
          subject.getSession(), true, null, null, SecurityUtils.getSecurityManager());
    } catch (IOException e) {
      LOGGER.error(MessageFormat.format("Unable to parse user details from validated token {0}",
          response.readEntity(String.class)), e);
      throw new ApiException("Unable to validate user", e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see gov.ca.cwds.rest.resources.auth.LoginResourceHelper#callback(javax.servlet.http.
   * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String, java.lang.String)
   */
  @Override
  public Response callback(HttpServletRequest request, HttpServletResponse response, String code,
      String state) {

    String retval = UNAUTHORIZED;
    LOGGER.info("safCallback(): ENTER: code=" + code + ", state=" + state);

    if (code.isEmpty() || state.isEmpty() || !pendingAuthByState.containsKey(state)) {
      LOGGER.info("INTRUDER ALERT!");
      return Response.status(Status.BAD_REQUEST).entity(retval).build();
    }

    // Track callback.
    SAFAuthBean auth = pendingAuthByState.get(state);
    auth.setCode(code);
    auth.setCalledCallbackAt(new Date().getTime());
    pendingAuthByCode.put(code, auth);

    WebTarget target = client.target(retrieveTokenUrl);
    Form form = new Form();
    form.param("client_id", clientId.toUpperCase());
    form.param("client_secret", clientSecret);
    form.param("code", code);
    form.param("grant_type", "authorization_code");
    form.param("redirect_uri", callbackUrl);
    form.param("state", state);

    final OauthResponse oauth = target.request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), OauthResponse.class);

    if (StringUtils.isBlank(oauth.getAccessToken())) {
      LOGGER.error("NO ACCESS TOKEN! state=" + state + ", code=" + code);
      return Response.status(Status.UNAUTHORIZED).entity(UNAUTHORIZED).build();
    }

    LOGGER.warn("ACCESS GRANT! state=" + state + ", code=" + code + ", access token="
        + oauth.getAccessToken());

    auth.setValidatedAt(new Date().getTime());
    auth.setAccessToken(oauth.getAccessToken());
    auth.setRefreshToken(oauth.getRefreshToken());
    auth.setExpiresIn(oauth.getExpiresIn());
    auth.setExpiresOn(oauth.getExpiresOn());
    auth.setTokenType(oauth.getTokenType());

    try {
      LOGGER.info("safCallback(): Success! Redirect to landing page!");
      final String authHeader = oauth.getTokenType() + " " + oauth.getAccessToken();
      LOGGER.info("safCallback(): CWDS_AUTH: " + authHeader);
      response.setHeader("Authorization", authHeader);

      pendingAuthByCode.remove(code);
      pendingAuthByState.remove(state);

      authByAccessToken.put(auth.getAccessToken(), auth);


      StringBuilder buf = new StringBuilder();
      buf.append(urlByState.remove(state)).append("?token=").append(auth.getAccessToken());

      LOGGER.info("redirecting user to {}", buf.toString());
      response.sendRedirect(buf.toString());
      return Response.ok(retval).build();
    } catch (Exception e) {
      LOGGER.error("EXCEPTION: " + e.getMessage(), e);
      return Response.status(Status.UNAUTHORIZED).entity(UNAUTHORIZED).build();
    }

  }

  /**
   *
   * @return unique "state" value, used to track OAuth2 authentication requests.
   */
  private String state() {
    return UUID.randomUUID().toString();
  }
}
