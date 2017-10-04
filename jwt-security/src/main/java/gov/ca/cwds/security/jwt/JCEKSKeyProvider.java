package gov.ca.cwds.security.jwt;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Created by dmitry.rudenko on 6/30/2017.
 */
class JCEKSKeyProvider implements KeyProvider {
  private JwtConfiguration configuration;

  public JCEKSKeyProvider(JwtConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public PrivateKey getSigningKey() {
    try {
      return (PrivateKey) getKeyStore().getKey(configuration.getKeyStore().getAlias(), configuration.getKeyStore().getKeyPassword().toCharArray());
    } catch (Exception e) {
      throw new JwtException(e);
    }
  }

  @Override
  public PublicKey getValidatingKey() {
    try {
      return getKeyStore().getCertificate(configuration.getKeyStore().getAlias()).getPublicKey();
    } catch (Exception e) {
      throw new JwtException(e);
    }
  }

  @Override
  public SecretKey getEncryptingKey() {
    try {
      return (SecretKey) getKeyStore().getKey(configuration.getKeyStore().getEncAlias(), configuration.getKeyStore().getEncKeyPassword().toCharArray());
    } catch (Exception e) {
      throw new JwtException(e);
    }
  }

  @SuppressFBWarnings("UNVALIDATED_REDIRECT")
  private KeyStore getKeyStore() {
    try {
      KeyStore ks = KeyStore.getInstance("JCEKS");
      try (InputStream readStream = new FileInputStream(configuration.getKeyStore().getPath())) {
        char[] keyPassword = configuration.getKeyStore().getPassword().toCharArray();
        ks.load(readStream, keyPassword);
        return ks;
      }
    } catch (Exception e) {
      throw new JwtException(e);
    }
  }
}
