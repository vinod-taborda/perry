package gov.ca.cwds.perry;

import java.util.Date;

public class SAFAuthBean {

  private String code;
  private String state;

  private String accessToken;
  private String refreshToken;

  private long expiresIn;
  private long expiresOn;
  private String tokenType;

  private long calledAuthAt;
  private long calledCallbackAt;
  private long validatedAt;
  
  private boolean fakeRequest;

  public SAFAuthBean() {
    setCalledAuthAt(new Date().getTime());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((state == null) ? 0 : state.hashCode());
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
    SAFAuthBean other = (SAFAuthBean) obj;
    if (code == null) {
      if (other.code != null)
        return false;
    } else if (!code.equals(other.code))
      return false;
    if (state == null) {
      if (other.state != null)
        return false;
    } else if (!state.equals(other.state))
      return false;
    return true;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public long getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(long expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public long getExpiresOn() {
    return expiresOn;
  }

  public void setExpiresOn(long expiresOn) {
    this.expiresOn = expiresOn;
  }

  public long getCalledAuthAt() {
    return calledAuthAt;
  }

  public void setCalledAuthAt(long createdAt) {
    this.calledAuthAt = createdAt;
  }

  public long getValidatedAt() {
    return validatedAt;
  }

  public void setValidatedAt(long validatedAt) {
    this.validatedAt = validatedAt;
  }

  public long getCalledCallbackAt() {
    return calledCallbackAt;
  }

  public void setCalledCallbackAt(long calledCallbackAt) {
    this.calledCallbackAt = calledCallbackAt;
  }

  @Override
  public String toString() {
    return "SAFAuthBean [code=" + code + ", state=" + state + ", accessToken=" + accessToken
        + ", refreshToken=" + refreshToken + ", expiresIn=" + expiresIn + ", tokenType=" + tokenType
        + ", expiresOn=" + expiresOn + ", calledAuthAt=" + calledAuthAt + ", calledCallbackAt="
        + calledCallbackAt + ", validatedAt=" + validatedAt + "]";
  }

  public boolean isFakeRequest() {
    return fakeRequest;
  }

  public void setFakeRequest(boolean fakeRequest) {
    this.fakeRequest = fakeRequest;
  }

}
