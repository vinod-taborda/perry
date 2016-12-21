package gov.ca.cwds.perry;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cwds")
public class OauthController {

  private static final Logger LOG = LoggerFactory.getLogger(OauthController.class);

  private static final String THIS_PROTOCOL = "http://";
  private static final String THIS_HOST = "somehost";
  private static final String THIS_PATH = "/cwds/";
  private static final String BASE_URL = THIS_PROTOCOL + THIS_HOST + THIS_PATH;

  private static final String SAF_BASE = "https://sectest.dss.ca.gov/web1/dss_saf/auth/v2/";
  private static final String SAF_AUTH_URL = SAF_BASE + "oauth2/authorize";
  private static final String SAF_TOKEN_URL = SAF_BASE + "token";
  private static final String SAF_VALIDATE_URL =
      "https://sectest.dss.ca.gov/web1/dss_saf/data/v2/api/client/41/auth/validatetoken";

  private static final String INTAKE_REDIRECT_URL = BASE_URL + "landing";
  private static final String FAKE_SAF_AUTH_URL = BASE_URL + "fakeauthorize";
  private static final String FAKE_SAF_CALLBACK = BASE_URL + "fakecallback";
  private static final String FAKE_SAF_TOKEN_URL = BASE_URL + "faketoken";
  private static final String FAKE_INTAKE_REDIRECT_URL = BASE_URL + "/fake_landing";

  // TODO: Don't store credentials in plain text or unprotected memory.
  private static final String SAF_CLIENT_ID = "someclientid";
  private static final String SAF_CLIENT_SECRET = "somesecret";

  // Registered with SAF.
  private static final String SAF_CALLBACK = BASE_URL + "callback";
  private static final String reponseType = "code";
  private static final String basicProfile = "basic_profile";

  @Autowired
  protected Client client;

  /**
   * key = "state" sent to SAF.
   */
  protected Map<String, SAFAuthBean> pendingAuthByState =
      new ConcurrentHashMap<String, SAFAuthBean>();

  /**
   * key = code from SAF callback.
   */
  protected Map<String, SAFAuthBean> pendingAuthByCode =
      new ConcurrentHashMap<String, SAFAuthBean>();

  /**
   * key = access token.
   */
  protected Map<String, SAFAuthBean> authByAccessToken =
      new ConcurrentHashMap<String, SAFAuthBean>();

  /**
   * 
   * @return unique "state" value, used to track OAuth2 authentication requests.
   */
  protected String getUniqueId() {
    return UUID.randomUUID().toString();
  }

  /**
   * Validate an unknown access token with SAF.
   * 
   * @param token
   * @return
   */
  protected boolean validateToken(String token) {
    Response response = client.target(SAF_VALIDATE_URL).request()
        .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
        .post(Entity.entity("bearer " + token, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

    if (!response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
      LOG.info("TOKEN VALIDATION FAILED! " + response);
      return false;
    }

    return true;
  }

  @RequestMapping("/login")
  public void login(HttpServletResponse response) throws IOException {

    // Track this login request.
    final String state = getUniqueId();
    SAFAuthBean auth = new SAFAuthBean();
    auth.setState(state);
    pendingAuthByState.put(state, auth);

    String fullUrl =
        SAF_AUTH_URL + "?" + "client_id=" + SAF_CLIENT_ID + "&response_type=" + reponseType
            + "&redirect_uri=" + SAF_CALLBACK + "&scope=" + basicProfile + "&state=" + state;

    LOG.info("login(): redirect to " + fullUrl);
    response.sendRedirect(fullUrl);
  }

  @GET
  @RequestMapping("/callback")
  // @Produces({MediaType.APPLICATION_FORM_URLENCODED})
  // public Response safCallback(HttpServletResponse response,
  public String safCallback(HttpServletRequest req, HttpServletResponse resp,
      @RequestParam(value = "code", defaultValue = "") String code,
      @RequestParam(value = "state", defaultValue = "") String state) throws IOException {

    String retval = "Unauthorized";
    LOG.info("safCallback(): ENTER: code=" + code + ", state=" + state);

    if (code.isEmpty() || state.isEmpty() || !pendingAuthByState.containsKey(state)) {
      LOG.info("safCallback(): INTRUDER ALERT!");
      resp.setStatus(HttpStatus.BAD_REQUEST.value());
      return retval;
    }

    // Track callback.
    SAFAuthBean auth = pendingAuthByState.get(state);
    auth.setCode(code);
    auth.setCalledCallbackAt(new Date().getTime());
    pendingAuthByCode.put(code, auth);

    LOG.info("SAF_TOKEN_URL=" + SAF_TOKEN_URL);
    WebTarget target = client.target(SAF_TOKEN_URL);
    Form form = new Form();
    form.param("client_id", SAF_CLIENT_ID);
    form.param("client_secret", SAF_CLIENT_SECRET);
    form.param("code", code);
    form.param("grant_type", "authorization_code");
    form.param("redirect_uri", SAF_CALLBACK);

    final OauthResponse oauth = target.request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), OauthResponse.class);

    if (!StringUtils.isEmpty(oauth.getAccessToken())) {
      LOG.warn("ACCESS GRANT! state=" + state + ", code=" + code + ", access token="
          + oauth.getAccessToken());

      auth.setValidatedAt(new Date().getTime());
      auth.setAccessToken(oauth.getAccessToken());
      auth.setRefreshToken(oauth.getRefreshToken());
      auth.setExpiresIn(oauth.getExpiresIn());
      auth.setExpiresOn(oauth.getExpiresOn());
      auth.setTokenType(oauth.getTokenType());

      // SAF DOES NOT AUTOMATICALLY REDIRECT THE USER TO GIVEN REDIRECT_URL!
      // Send user to landing page.
      try {
        LOG.info("safCallback(): Success! Redirect to landing page!");
        final String authHeader = oauth.getTokenType() + " " + oauth.getAccessToken();
        LOG.info("safCallback(): CWDS_AUTH: " + authHeader);
        resp.setHeader("CWDS_AUTH", authHeader);

        pendingAuthByCode.remove(code);
        pendingAuthByState.remove(state);
        authByAccessToken.put(auth.getAccessToken(), auth);

        // ANSWER: SAF does not obey redirects.
        // Instead, return HTML to the browser with redirect instructions.

        // Absolute URL:
        // response.sendRedirect(INTAKE_REDIRECT_URL);

        // Relative URI:
        // LOGGER.info("redirect to *relative* URI ...");
        // final String landingPage = response.encodeURL("landing");
        // response.setCharacterEncoding("UTF-8");
        // response.sendRedirect(landingPage);

        // final int status = response.getStatus();
        // LOGGER.info("After redirect ... status=" + status);

        // retval = Response.seeOther(UriBuilder.fromPath("landing").build())
        // .header("CWDS_AUTH", authHeader).build();

        retval = redirectToLanding(req, resp);
        resp.sendRedirect("/protected");
        return retval;
      } catch (Exception e) {
        LOG.error("EXCEPTION: " + e.getMessage(), e);
        resp.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
      }
    } else {
      LOG.error("NO ACCESS TOKEN! state=" + state + ", code=" + code);
      resp.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
    }

    LOG.info("safCallback(): EXIT");
    return retval;
  }

  protected String redirectToLanding(HttpServletRequest req, HttpServletResponse resp) {
    // req.getProtocol()
    // req.getRemoteHost()
    // return "<!DOCTYPE HTML><html lang=\"en-US\"><head>"
    // + "<meta charset=\"UTF-8\">"
    // + "<meta http-equiv=\"refresh\" content=\"5;url=http://54.70.247.41/cwds/landing\">"
    // + "<script type=\"text/javascript\">"
    // + " window.location.href = \"http://54.70.247.41/cwds/landing\""
    // + "</script><title>Page Redirection</title></head>"
    // + "<body>If you are not redirected automatically, follow the <a
    // href='http://54.70.247.41/cwds/landing'>link to example</a>"
    // + "<p/>" + new Date() + "</body></html>";
    return "<!DOCTYPE HTML><html lang=\"en-US\"><head></head><body>CWDS Landing<p/>" + new Date()
        + "</body></html>";
  }

  @RequestMapping("/protected")
  protected String protectedPage(HttpServletRequest req, HttpServletResponse resp) {
    return "<!DOCTYPE HTML><html lang=\"en-US\"><head></head><body>CWDS Protected Resource<p/>"
        + new Date() + "</body></html>";
  }

  @RequestMapping("/landing")
  public List<Employee> getEmployees(@RequestHeader(value = "CWDS_AUTH") String token,
      HttpServletResponse response) throws IOException {

    LOG.info("getEmployees(): ENTER: token=" + token);
    if (!validateToken(token)) {
      LOG.error("getEmployees(): INVALID ACCESS TOKEN!!!");
      response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Access Token");
    }

    LOG.trace("getEmployees(): EXIT");
    return Employee.EMPLOYEES;
  }

  @RequestMapping("/test")
  public String test() {
    return "up and running. " + new Date();
  }

  // ====================
  // SAF SIMULATOR:
  // ====================

  @RequestMapping("/local-fakeauthorize")
  public OauthResponse localfakeAuthorize(HttpServletResponse response) {

    // Call local callback.
    WebTarget target = client.target("http://localhost:8080/cwds/fakeSAF");
    Form form = new Form();
    form.param("code", "132456789");
    form.param("state", "456132789");
    form.param("grant_type", "authorization_code");

    OauthResponse oauth = target.request()
        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), OauthResponse.class);

    System.out.println(oauth.toString());
    return oauth;
  }

  @RequestMapping("/fakeSAF")
  public OauthResponse testSAFRedirect() {
    return new OauthResponse(
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik5HVEZ2ZEstZnl0aEV1THdqcHdBSk9NOW4tQSJ9.eyJhdWQiOiJodHRwczovL3NlcnZpY2UuY29udG9zby5jb20vIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvN2ZlODE0NDctZGE1Ny00Mzg1LWJlY2ItNmRlNTdmMjE0NzdlLyIsImlhdCI6MTM4ODQ0MDg2MywibmJmIjoxMzg4NDQwODYzLCJleHAiOjEzODg0NDQ3NjMsInZlciI6IjEuMCIsInRpZCI6IjdmZTgxNDQ3LWRhNTctNDM4NS1iZWNiLTZkZTU3ZjIxNDc3ZSIsIm9pZCI6IjY4Mzg5YWUyLTYyZmEtNGIxOC05MWZlLTUzZGQxMDlkNzRmNSIsInVwbiI6ImZyYW5rbUBjb250b3NvLmNvbSIsInVuaXF1ZV9uYW1lIjoiZnJhbmttQGNvbnRvc28uY29tIiwic3ViIjoiZGVOcUlqOUlPRTlQV0pXYkhzZnRYdDJFYWJQVmwwQ2o4UUFtZWZSTFY5OCIsImZhbWlseV9uYW1lIjoiTWlsbGVyIiwiZ2l2ZW5fbmFtZSI6IkZyYW5rIiwiYXBwaWQiOiIyZDRkMTFhMi1mODE0LTQ2YTctODkwYS0yNzRhNzJhNzMwOWUiLCJhcHBpZGFjciI6IjAiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJhY3IiOiIxIn0.JZw8jC0gptZxVC-7l5sFkdnJgP3_tRjeQEPgUn28XctVe3QqmheLZw7QVZDPCyGycDWBaqy7FLpSekET_BftDkewRhyHk9FW_KeEz0ch2c3i08NGNDbr6XYGVayNuSesYk5Aw_p3ICRlUV1bqEwk-Jkzs9EEkQg4hbefqJS6yS1HoV_2EsEhpd_wCQpxK89WPs3hLYZETRJtG5kvCCEOvSHXmDE6eTHGTnEgsIk--UlPe275Dvou4gEAwLofhLDQbMSjnlV5VLsjimNBVcSRFShoxmQwBJR_b2011Y5IuD6St5zPnzruBbZYkGNurQK63TJPWmRd3mbJsGM0mf3CUQ",
        "AwABAAAAvPM1KaPlrEqdFSBzjqfTGAMxZGUTdM0t4B4rTfgV29ghDOHRc2B-C_hHeJaJICqjZ3mY2b_YNqmf9SoAylD1PycGCB90xzZeEDg6oBzOIPfYsbDWNf621pKo2Q3GGTHYlmNfwoc-OlrxK69hkha2CF12azM_NYhgO668yfcUl4VBbiSHZyd1NVZG5QTIOcbObu3qnLutbpadZGAxqjIbMkQ2bQS09fTrjMBtDE3D6kSMIodpCecoANon9b0LATkpitimVCrl-NyfN3oyG4ZCWu18M9-vEou4Sq-1oMDzExgAf61noxzkNiaTecM-Ve5cq6wHqYQjfV9DOz4lbceuYCAA",
        "Bearer", 3600, 1388444763L);
  }

  @RequestMapping("/fakelogin")
  public void fakeLogin(HttpServletResponse response) throws IOException {

    // Track this login request.
    final String state = getUniqueId();
    SAFAuthBean auth = new SAFAuthBean();
    auth.setState(state);
    auth.setFakeRequest(true);
    pendingAuthByState.put(state, auth);

    final String fullUrl = FAKE_SAF_AUTH_URL + "?" + "client_id=" + SAF_CLIENT_ID
        + "&response_type=" + reponseType + "&redirect_uri=" + FAKE_INTAKE_REDIRECT_URL + "&scope="
        + basicProfile + "&state=" + state;

    response.sendRedirect(fullUrl);
  }

  @RequestMapping("/fakeauthorize")
  public OauthResponse fakeAuthorize(HttpServletResponse response,
      @RequestParam(value = "client_id", defaultValue = "") String clientId,
      @RequestParam(value = "response_type", defaultValue = "code") String responseType,
      @RequestParam(value = "redirect_uri", defaultValue = "") String redirectUri,
      @RequestParam(value = "scope", defaultValue = "basic_profile") String scope,
      @RequestParam(value = "state", defaultValue = "") String state) {

    LOG.info("fakeAuthorize(): \nstate=" + state + ", responseType=" + responseType
        + ", redirectUri=" + redirectUri);

    if (state.isEmpty() || !pendingAuthByState.containsKey(state)) {
      LOG.info("fakeAuthorize(): INTRUDER ALERT!");
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      return null;
    }

    // Track fake authorization request.
    SAFAuthBean auth = pendingAuthByState.get(state);
    auth.setCode(getUniqueId());

    // Call local callback.
    WebTarget target = client.target(FAKE_SAF_CALLBACK);
    Form form = new Form();
    form.param("code", auth.getCode());
    form.param("state", auth.getState());
    form.param("grant_type", "authorization_code");

    OauthResponse oauth = target.request()
        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), OauthResponse.class);

    WebTarget target2 = client.target(FAKE_INTAKE_REDIRECT_URL);
    Form form2 = new Form();
    form2.param("code", auth.getCode());
    form2.param("state", auth.getState());
    form2.param("grant_type", "authorization_code");

    oauth = target2.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED),
        OauthResponse.class);

    auth.setAccessToken(oauth.getAccessToken());
    auth.setRefreshToken(oauth.getRefreshToken());
    auth.setExpiresIn(oauth.getExpiresIn());
    auth.setExpiresOn(oauth.getExpiresOn());
    auth.setTokenType(oauth.getTokenType());

    LOG.info("fakeAuthorize(): EXIT");
    return oauth;
  }

  @RequestMapping("/faketoken")
  public OauthResponse fakeToken(HttpServletResponse response,
      @RequestParam(value = "code", defaultValue = "") String code,
      @RequestParam(value = "grant_type", defaultValue = "") String grantType,
      @RequestParam(value = "client_id", defaultValue = "") String clientId,
      @RequestParam(value = "redirect_uri", defaultValue = "") String redirectUri,
      @RequestParam(value = "client_secret", defaultValue = "") String clientSecret) {

    LOG.info("fakeToken(): \ncode=" + code + ", grantType=" + grantType + ", redirectUri="
        + redirectUri);

    if (code.isEmpty() || !pendingAuthByCode.containsKey(code)) {
      LOG.info("fakeToken(): INTRUDER ALERT!");
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      return null;
    }

    // Track callback.
    SAFAuthBean auth = pendingAuthByCode.get(code);
    auth.setCode(code);
    auth.setCalledCallbackAt(new Date().getTime());

    WebTarget target = client.target(redirectUri);
    Form form = new Form();
    form.param("client-id", SAF_CLIENT_ID);
    form.param("client-secret", SAF_CLIENT_SECRET);
    form.param("code", code);
    form.param("grant_type", "authorization_code");

    final OauthResponse oauth = target.request()
        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), OauthResponse.class);

    auth.setAccessToken(oauth.getAccessToken());
    auth.setRefreshToken(oauth.getRefreshToken());
    auth.setExpiresIn(oauth.getExpiresIn());
    auth.setExpiresOn(oauth.getExpiresOn());
    auth.setTokenType(oauth.getTokenType());

    LOG.info("fakeToken(): EXIT");
    return oauth;
  }

  @RequestMapping("/dummy_token")
  @Produces({MediaType.APPLICATION_JSON})
  public String mocktoken() {
    return "{ \"access_token\": \"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ik5HVEZ2ZEstZnl0aEV1THdqcHdBSk9NOW4tQSJ9.eyJhdWQiOiJodHRwczovL3NlcnZpY2UuY29udG9zby5jb20vIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvN2ZlODE0NDctZGE1Ny00Mzg1LWJlY2ItNmRlNTdmMjE0NzdlLyIsImlhdCI6MTM4ODQ0MDg2MywibmJmIjoxMzg4NDQwODYzLCJleHAiOjEzODg0NDQ3NjMsInZlciI6IjEuMCIsInRpZCI6IjdmZTgxNDQ3LWRhNTctNDM4NS1iZWNiLTZkZTU3ZjIxNDc3ZSIsIm9pZCI6IjY4Mzg5YWUyLTYyZmEtNGIxOC05MWZlLTUzZGQxMDlkNzRmNSIsInVwbiI6ImZyYW5rbUBjb250b3NvLmNvbSIsInVuaXF1ZV9uYW1lIjoiZnJhbmttQGNvbnRvc28uY29tIiwic3ViIjoiZGVOcUlqOUlPRTlQV0pXYkhzZnRYdDJFYWJQVmwwQ2o4UUFtZWZSTFY5OCIsImZhbWlseV9uYW1lIjoiTWlsbGVyIiwiZ2l2ZW5fbmFtZSI6IkZyYW5rIiwiYXBwaWQiOiIyZDRkMTFhMi1mODE0LTQ2YTctODkwYS0yNzRhNzJhNzMwOWUiLCJhcHBpZGFjciI6IjAiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJhY3IiOiIxIn0.JZw8jC0gptZxVC-7l5sFkdnJgP3_tRjeQEPgUn28XctVe3QqmheLZw7QVZDPCyGycDWBaqy7FLpSekET_BftDkewRhyHk9FW_KeEz0ch2c3i08NGNDbr6XYGVayNuSesYk5Aw_p3ICRlUV1bqEwk-Jkzs9EEkQg4hbefqJS6yS1HoV_2EsEhpd_wCQpxK89WPs3hLYZETRJtG5kvCCEOvSHXmDE6eTHGTnEgsIk--UlPe275Dvou4gEAwLofhLDQbMSjnlV5VLsjimNBVcSRFShoxmQwBJR_b2011Y5IuD6St5zPnzruBbZYkGNurQK63TJPWmRd3mbJsGM0mf3CUQ\", \"token_type\": \"Bearer\", \"expires_in\": \"3600\", \"expires_on\": \"1388444763\", \"refresh_token\": \"AwABAAAAvPM1KaPlrEqdFSBzjqfTGAMxZGUTdM0t4B4rTfgV29ghDOHRc2B-C_hHeJaJICqjZ3mY2b_YNqmf9SoAylD1PycGCB90xzZeEDg6oBzOIPfYsbDWNf621pKo2Q3GGTHYlmNfwoc-OlrxK69hkha2CF12azM_NYhgO668yfcUl4VBbiSHZyd1NVZG5QTIOcbObu3qnLutbpadZGAxqjIbMkQ2bQS09fTrjMBtDE3D6kSMIodpCecoANon9b0LATkpitimVCrl-NyfN3oyG4ZCWu18M9-vEou4Sq-1oMDzExgAf61noxzkNiaTecM-Ve5cq6wHqYQjfV9DOz4lbceuYCAA\" }";
  }

}

