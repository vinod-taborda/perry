package gov.ca.cwds.security.jwt;

/**
 * Created by dmitry.rudenko on 6/30/2017.
 */
public class JwtConfiguration {
  private int timeout;
  private String issuer;
  private boolean encryptionEnabled;
  private String encryptionMethod;

  private KeyStoreConfiguration keyStore;

  public int getTimeout() {
    return timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public boolean isEncryptionEnabled() {
    return encryptionEnabled;
  }

  public void setEncryptionEnabled(boolean encryptionEnabled) {
    this.encryptionEnabled = encryptionEnabled;
  }

  public KeyStoreConfiguration getKeyStore() {
    return keyStore;
  }

  public void setKeyStore(KeyStoreConfiguration keyStore) {
    this.keyStore = keyStore;
  }

  public String getEncryptionMethod() {
    return encryptionMethod;
  }

  public void setEncryptionMethod(String encryptionMethod) {
    this.encryptionMethod = encryptionMethod;
  }

  public static class KeyStoreConfiguration {
    private String path;
    private String alias;
    private String password;
    private String keyPassword;
    private String encAlias;
    private String encKeyPassword;

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }

    public String getAlias() {
      return alias;
    }

    public void setAlias(String alias) {
      this.alias = alias;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getKeyPassword() {
      return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
      this.keyPassword = keyPassword;
    }

    public String getEncAlias() {
      return encAlias;
    }

    public void setEncAlias(String encAlias) {
      this.encAlias = encAlias;
    }

    public String getEncKeyPassword() {
      return encKeyPassword;
    }

    public void setEncKeyPassword(String encKeyPassword) {
      this.encKeyPassword = encKeyPassword;
    }
  }
}

