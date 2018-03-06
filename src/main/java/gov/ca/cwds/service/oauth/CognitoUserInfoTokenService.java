package gov.ca.cwds.service.oauth;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

@ConfigurationProperties(prefix = "cognito")
public class CognitoUserInfoTokenService extends CaresUserInfoTokenService {


  private String host;
  private String mediaSubtype;
  private String getUserTarget;

  private HttpHeaders headers;

  public CognitoUserInfoTokenService(ResourceServerProperties resourceServerProperties) {
    super(resourceServerProperties);
  }

  private HttpHeaders headers() {
    if (headers == null) {
      headers = new HttpHeaders();
      headers.set("HOST", host);
      headers.set("Content-Type", "application/" + mediaSubtype);
      headers.set("X-Amz-Target", getUserTarget);
    }
    return headers;
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected HttpEntity httpEntityForGetUser(String accessToken) {
    String json = String.format("{\"AccessToken\": \"%s\"}", accessToken);
    return new HttpEntity<String>(json, headers());
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getMediaSubtype() {
    return mediaSubtype;
  }

  public void setMediaSubtype(String mediaSubtype) {
    this.mediaSubtype = mediaSubtype;
  }

  public String getGetUserTarget() {
    return getUserTarget;
  }

  public void setGetUserTarget(String getUserTarget) {
    this.getUserTarget = getUserTarget;
  }

}
