package gov.ca.cwds.data.persistence.token;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by TPT2 on 10/24/2017.
 */
@Entity
@Table(name = "oauth_client_token")
public class OauthClientToken {
  @Id
  private String token_id;
  private String token;
  private byte[] authentication_id;
  private String user_name;
  private String client_id;

  public String getToken_id() {
    return token_id;
  }

  public void setToken_id(String token_id) {
    this.token_id = token_id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public byte[] getAuthentication_id() {
    return authentication_id;
  }

  public void setAuthentication_id(byte[] authentication_id) {
    this.authentication_id = authentication_id;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getClient_id() {
    return client_id;
  }

  public void setClient_id(String client_id) {
    this.client_id = client_id;
  }
}
