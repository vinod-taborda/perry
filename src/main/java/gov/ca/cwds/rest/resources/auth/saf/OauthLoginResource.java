package gov.ca.cwds.rest.resources.auth.saf;

import static gov.ca.cwds.rest.core.Api.RESOURCE_USER_AUTHENTICATION;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.rest.SAFConfiguration;
import gov.ca.cwds.rest.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = RESOURCE_USER_AUTHENTICATION, tags = {RESOURCE_USER_AUTHENTICATION})
@Path(value = RESOURCE_USER_AUTHENTICATION)
public class OauthLoginResource {

  private static final String UNAUTHORIZED = "Unauthorized";

  private static final String BEARER = "bearer ";

  private static final Logger LOGGER = LoggerFactory.getLogger(OauthLoginResource.class);

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
  private final Map<String, SAFAuthBean> pendingAuthByState = new ConcurrentHashMap<>();

  /**
   * key = code from SAF callback.
   */
  private final Map<String, SAFAuthBean> pendingAuthByCode = new ConcurrentHashMap<>();

  /**
   * key = access token.
   */
  private final Map<String, SAFAuthBean> authByAccessToken = new ConcurrentHashMap<>();

  private Client client;

  /**
   * Constructor
   * 
   * @param safConfiguration Configuration class containing the details needed to interact with SAF
   * @param client A jersey client for making http calls to SAF
   */
  @Inject
  public OauthLoginResource(final SAFConfiguration safConfiguration, final Client client) {
    this.client = client;
    this.authUrl = safConfiguration.getBaseUrl() + safConfiguration.getAuthPath();
    this.retrieveTokenUrl = safConfiguration.getBaseUrl() + safConfiguration.getRetrieveTokenPath();
    this.validateTokenUrl = safConfiguration.getBaseUrl() + safConfiguration.getValidateTokenPath();
    this.clientId = safConfiguration.getClientId();
    this.clientSecret = safConfiguration.getClientSecret();
    this.callbackUrl = safConfiguration.getCallbackUrl();
    this.scope = safConfiguration.getScope();
  }

  /**
   * Validate an unknown access token with SAF.
   * 
   * @param token
   * @return
   */
  protected boolean validateTokenForAPI(String token) {
    Response response =
        client.target(validateTokenUrl).request().header(HttpHeaders.AUTHORIZATION, BEARER + token)
            .post(Entity.entity(BEARER + token, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

    if (response.getStatus() != (Status.OK.getStatusCode())) {
      LOGGER.info("TOKEN VALIDATION FAILED! " + response);
      return false;
    }

    return true;
  }

  @SuppressWarnings("static-access")
  @GET
  @Path("/validateToken")
  @Consumes(value = MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "validate Token by accesstoken")
  public Response validateToken(@ApiParam(required = true, name = "token",
      value = "the value of the accesstoken") @QueryParam("token") String token) {
    Response response =
        client.target(validateTokenUrl).request().header(HttpHeaders.AUTHORIZATION, BEARER + token)
            .post(Entity.entity(BEARER + token, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

    if (response.getStatus() != (Status.OK.getStatusCode())) {
      LOGGER.info("TOKEN VALIDATION FAILED! " + response);
      return response.status(Status.UNAUTHORIZED).build();
    }

    return response;
  }

  @GET
  @Path("/login")
  public void login(@Context final HttpServletResponse response) {

    final String state = state();
    SAFAuthBean auth = new SAFAuthBean();
    auth.setState(state);
    pendingAuthByState.put(state, auth);

    String fullUrl = "";

    try {
      // TODO : clean this up
      StringBuilder buf = new StringBuilder();
      buf.append(authUrl).append("?client_id=").append(clientId).append("&response_type=")
          .append(RESPONSE_TYPE).append("&redirect_uri=")
          .append(URLEncoder.encode(callbackUrl, ENCODING)).append("&scope=").append(scope)
          .append("&state=").append(state);
      fullUrl = buf.toString();
      // fullUrl = authUrl + "?" + "client_id=" + clientId + "&response_type=" + RESPONSE_TYPE
      // + "&redirect_uri=" + URLEncoder.encode(callbackUrl, ENCODING) + "&scope=" + scope
      // + "&state=" + state;

      response.sendRedirect(fullUrl);
    } catch (UnsupportedEncodingException e) {
      LOGGER.error("Uknown Encoding {}", ENCODING);
      throw new ApiException("Unknown Encoding", e);
    } catch (IOException e) {
      throw new ApiException("unalbe to redirect to the url:" + fullUrl, e);
    }
  }

  @GET
  @Path("/callback")
  public Response safCallback(@Context HttpServletRequest request,
      @Context HttpServletResponse response, @QueryParam(value = "code") String code,
      @QueryParam(value = "state") String state) {

    String retval = UNAUTHORIZED;
    LOGGER.info("safCallback(): ENTER: code=" + code + ", state=" + state);

    if (code.isEmpty() || state.isEmpty() || !pendingAuthByState.containsKey(state)) {
      LOGGER.info("safCallback(): INTRUDER ALERT!");
      return Response.status(Status.BAD_REQUEST).entity(retval).build();
    }

    // Track callback.
    SAFAuthBean auth = pendingAuthByState.get(state);
    auth.setCode(code);
    auth.setCalledCallbackAt(new Date().getTime());
    pendingAuthByCode.put(code, auth);

    WebTarget target = client.target(retrieveTokenUrl);
    Form form = new Form();
    form.param("client_id", clientId);
    form.param("client_secret", clientSecret);
    form.param("code", code);
    form.param("grant_type", "authorization_code");
    form.param("redirect_uri", callbackUrl);

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

    // SAF DOES NOT AUTOMATICALLY REDIRECT THE USER TO GIVEN
    // REDIRECT_URL!
    // Send user to landing page.
    try {
      LOGGER.info("safCallback(): Success! Redirect to landing page!");
      final String authHeader = oauth.getTokenType() + " " + oauth.getAccessToken();
      LOGGER.info("safCallback(): CWDS_AUTH: " + authHeader);
      response.setHeader("CWDS_AUTH", authHeader);

      pendingAuthByCode.remove(code);
      pendingAuthByState.remove(state);
      authByAccessToken.put(auth.getAccessToken(), auth);

      retval = redirectToLanding(request, response);
      return Response.ok(retval).build();
    } catch (Exception e) {
      LOGGER.error("EXCEPTION: " + e.getMessage(), e);
      return Response.status(Status.UNAUTHORIZED).entity(UNAUTHORIZED).build();
    }

  }

  protected String redirectToLanding(HttpServletRequest req, HttpServletResponse resp) {
    return "<!DOCTYPE HTML><html lang=\"en-US\"><head></head><body>CWDS Landing<p/>" + new Date()
        + "</body></html>";
  }

  /**
   * 
   * @return unique "state" value, used to track OAuth2 authentication requests.
   */
  private String state() {
    return UUID.randomUUID().toString();
  }

  public String getAuthUrl() {
    return authUrl;
  }

  public synchronized void setAuthUrl(String authUrl) {
    this.authUrl = authUrl;
  }

}
