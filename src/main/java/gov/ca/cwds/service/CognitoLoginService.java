package gov.ca.cwds.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@Primary
@ConfigurationProperties(prefix = "cognito")
public class CognitoLoginService extends LoginServiceImpl {

  private String host;
  private String mediaSubtype;
  private String revokeTokenTarget;

  private HttpHeaders headers;

  private HttpHeaders headers() {
    if (headers == null) {
      headers = new HttpHeaders();
      headers.set("HOST", host);
      headers.set("Content-Type", "application/" + mediaSubtype);
      headers.set("X-Amz-Target", revokeTokenTarget);
    }
    return headers;
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected HttpEntity httpEntityForInvalidation(OAuth2AccessToken accessToken) {

    String json = String.format("{\"AccessToken\": \"%s\"}", accessToken);
    return new HttpEntity<String>(json, headers());
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getRevokeTokenTarget() {
    return revokeTokenTarget;
  }

  public void setRevokeTokenTarget(String revokeTokenTarget) {
    this.revokeTokenTarget = revokeTokenTarget;
  }

  public String getMediaSubtype() {
    return mediaSubtype;
  }

  public void setMediaSubtype(String mediaSubtype) {
    this.mediaSubtype = mediaSubtype;
  }
}
