package gov.ca.cwds.security.realm;

import gov.ca.cwds.security.jwt.JwtConfiguration;
import gov.ca.cwds.security.jwt.JwtService;
import org.apache.shiro.authc.AuthenticationException;

/**
 * Implementation of JWT processing realm. This realm validates JWT token and extract identity claim
 * from JWT payload. Identity claim example: {"user" : "username", "roles" : ["role1", "role2"] }
 * <p>
 * During authentication process this realm puts user name as primary credential and mapped identity
 * claim as secondary. So authorization process will expect 2 principals.
 */
public class JwtRealm extends AbstractRealm {

  private String keyStorePath;
  private String keyStoreAlias;
  private String keyStorePassword;
  private String keyStoreKeyPassword;
  private boolean useEncryption;
  private String encKeyAlias;
  private String encKeyPassword;
  private String encryptionMethod;
  private String tokenIssuer;
  private boolean headlessToken = true;

  JwtService jwtService;

  @Override
  protected void onInit() {
    super.onInit();
    jwtService = new JwtService(jwtConfiguration());
  }

  protected JwtConfiguration jwtConfiguration() {
    JwtConfiguration jwtConfiguration = new JwtConfiguration();
    jwtConfiguration.setKeyStore(new JwtConfiguration.KeyStoreConfiguration());
    jwtConfiguration.setEncryptionMethod(encryptionMethod);
    jwtConfiguration.setEncryptionEnabled(useEncryption);
    jwtConfiguration.setIssuer(tokenIssuer);
    jwtConfiguration.setHeadless(headlessToken);
    jwtConfiguration.getKeyStore().setPassword(keyStorePassword);
    jwtConfiguration.getKeyStore().setAlias(keyStoreAlias);
    jwtConfiguration.getKeyStore().setPath(keyStorePath);
    jwtConfiguration.getKeyStore().setKeyPassword(keyStoreKeyPassword);
    jwtConfiguration.getKeyStore().setEncAlias(encKeyAlias);
    jwtConfiguration.getKeyStore().setEncKeyPassword(encKeyPassword);
    return jwtConfiguration;
  }

  /**
   * @return the keyStorePath
   */
  public String getKeyStorePath() {
    return keyStorePath;
  }

  /**
   * @param keyStorePath - keyStorePath
   */
  public void setKeyStorePath(String keyStorePath) {
    this.keyStorePath = keyStorePath;
  }

  /**
   * @return the keyStoreAlias
   */
  public String getKeyStoreAlias() {
    return keyStoreAlias;
  }

  /**
   * @param keyStoreAlias - keyStoreAlias
   */
  public void setKeyStoreAlias(String keyStoreAlias) {
    this.keyStoreAlias = keyStoreAlias;
  }

  /**
   * @return the keyStorePassword
   */
  public String getKeyStorePassword() {
    return keyStorePassword;
  }

  /**
   * @param keyStorePassword - keyStorePassword
   */
  public void setKeyStorePassword(String keyStorePassword) {
    this.keyStorePassword = keyStorePassword;
  }

  /**
   * @return the keyStoreKeyPassword
   */
  public String getKeyStoreKeyPassword() {
    return keyStoreKeyPassword;
  }

  /**
   * @param keyStoreKeyPassword - keyStoreKeyPassword
   */
  public void setKeyStoreKeyPassword(String keyStoreKeyPassword) {
    this.keyStoreKeyPassword = keyStoreKeyPassword;
  }

  /**
   * @return the tokenIssuer
   */
  public String getTokenIssuer() {
    return tokenIssuer;
  }

  /**
   * @param tokenIssuer - tokenIssuer
   */
  public void setTokenIssuer(String tokenIssuer) {
    this.tokenIssuer = tokenIssuer;
  }

  public boolean isUseEncryption() {
    return useEncryption;
  }

  public void setUseEncryption(boolean useEncryption) {
    this.useEncryption = useEncryption;
  }

  public String getEncKeyAlias() {
    return encKeyAlias;
  }

  public void setEncKeyAlias(String encKeyAlias) {
    this.encKeyAlias = encKeyAlias;
  }

  public String getEncKeyPassword() {
    return encKeyPassword;
  }

  public void setEncKeyPassword(String encKeyPassword) {
    this.encKeyPassword = encKeyPassword;
  }

  public String getEncryptionMethod() {
    return encryptionMethod;
  }

  public void setEncryptionMethod(String encryptionMethod) {
    this.encryptionMethod = encryptionMethod;
  }

  public boolean isHeadlessToken() {
    return headlessToken;
  }

  public void setHeadlessToken(boolean headlessToken) {
    this.headlessToken = headlessToken;
  }

  protected String validate(String token)  {
    try {
      return jwtService.validate(token);
    } catch (Exception e) {
      throw new AuthenticationException(e);
    }
  }

}
