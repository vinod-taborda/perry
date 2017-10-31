package gov.ca.cwds.data.reissue.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by TPT2 on 10/24/2017.
 */
@Entity
@Table(name = "perry_token")
public class PerryTokenEntity implements Serializable {
  @Id
  @Column(name = "token")
  private String token;
  @Column(name = "access_code")
  private String accessCode;
  @Column(name = "user_id")
  private String user;
  @Lob
  @Column(name = "access_token")
  private Serializable accessToken;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_date")
  private Date createdDate = new Date();

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public Serializable getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(Serializable accessToken) {
    this.accessToken = accessToken;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getAccessCode() {
    return accessCode;
  }

  public void setAccessCode(String accessCode) {
    this.accessCode = accessCode;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
