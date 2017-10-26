package gov.ca.cwds.data.persistence.token;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.SerializationUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by TPT2 on 10/24/2017.
 */
@Entity
@Table(name = "perry_token")
public class PerryTokenEntity implements Serializable {
  @Id
  private String token;
  private String user;
  private byte[] accessToken;
  private Date createdDate;
  private TokenType tokenType;

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public byte[] getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(byte[] accessToken) {
    this.accessToken = accessToken;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public TokenType getTokenType() {
    return tokenType;
  }

  public void setTokenType(TokenType tokenType) {
    this.tokenType = tokenType;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
