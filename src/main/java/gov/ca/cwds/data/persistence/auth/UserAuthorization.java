package gov.ca.cwds.data.persistence.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import gov.ca.cwds.data.persistence.PersistentObject;
import gov.ca.cwds.data.persistence.cms.CmsPersistentObject;

/**
 * {@link PersistentObject} representing a User Authorization.
 * 
 * <p>
 * <strong>NOTE: </strong>This class is just a placeholder for now.
 * </p>
 * 
 * @author CWDS API Team
 */
@SuppressWarnings("serial")
@Entity
public class UserAuthorization extends CmsPersistentObject {

  @Id
  @Column(name = "USER_ID")
  private String userId;

  private String staffPersonId;

  private String socialWorker;

  private String supervisor;

  private String overrideAuthority;

  /**
   * Default constructor
   * 
   * Required for Hibernate
   */
  public UserAuthorization() {
    super();
  }

  public UserAuthorization(String userId, String staffPersonId, String socialWorker,
      String supervisor, String overrideAuthority) {
    super();
    this.userId = userId;
    this.staffPersonId = staffPersonId;
    this.socialWorker = socialWorker;
    this.supervisor = supervisor;
    this.overrideAuthority = overrideAuthority;
  }

  @Override
  public Serializable getPrimaryKey() {
    return null;
  }

}
