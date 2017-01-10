package gov.ca.cwds.rest.resources.auth.saf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class OauthResponse {

  private long expiresOn;
  private String accessToken, refreshToken, tokenType;
  private int expiresIn;

  /**
   * Required for JAX-WS JSON unmarshalling.
   */
  public OauthResponse() {

  }

  @JsonCreator
  public OauthResponse(@JsonProperty("access_token") String accessToken,
      @JsonProperty("refresh_token") String refreshToken,
      @JsonProperty("token_type") String tokenType, @JsonProperty("expires_in") int expiresIn,
      @JsonProperty("expires_on") long expiresOn) {
    this.expiresOn = expiresOn;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.tokenType = tokenType;
  }

  public long getExpiresOn() {
    return expiresOn;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  @JsonProperty("expires_on")
  public void setExpiresOn(long expiresOn) {
    this.expiresOn = expiresOn;
  }

  @JsonProperty("access_token")
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  @JsonProperty("refresh_token")
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  @JsonProperty("token_type")
  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  @JsonProperty("expires_in")
  public void setExpiresIn(int expiresIn) {
    this.expiresIn = expiresIn;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accessToken == null) ? 0 : accessToken.hashCode());
    result = prime * result + expiresIn;
    result = prime * result + (int) (expiresOn ^ (expiresOn >>> 32));
    result = prime * result + ((refreshToken == null) ? 0 : refreshToken.hashCode());
    result = prime * result + ((tokenType == null) ? 0 : tokenType.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OauthResponse other = (OauthResponse) obj;
    if (accessToken == null) {
      if (other.accessToken != null)
        return false;
    } else if (!accessToken.equals(other.accessToken))
      return false;
    if (expiresIn != other.expiresIn)
      return false;
    if (expiresOn != other.expiresOn)
      return false;
    if (refreshToken == null) {
      if (other.refreshToken != null)
        return false;
    } else if (!refreshToken.equals(other.refreshToken))
      return false;
    if (tokenType == null) {
      if (other.tokenType != null)
        return false;
    } else if (!tokenType.equals(other.tokenType))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "OauthResponse [expiresOn=" + expiresOn + ", accessToken=" + accessToken
        + ", refreshToken=" + refreshToken + ", tokenType=" + tokenType + ", expiresIn=" + expiresIn
        + "]";
  }

}
