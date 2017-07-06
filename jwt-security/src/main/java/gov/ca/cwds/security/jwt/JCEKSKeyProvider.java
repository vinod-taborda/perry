package gov.ca.cwds.security.jwt;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by dmitry.rudenko on 6/30/2017.
 */
class JCEKSKeyProvider implements KeyProvider {
  private JwtConfiguration configuration;

  public JCEKSKeyProvider(JwtConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public PrivateKey getSigningKey() throws Exception {
    return (PrivateKey) getKeyStore().getKey(configuration.getKeyStore().getAlias(), configuration.getKeyStore().getKeyPassword().toCharArray());
  }

  @Override
  public PublicKey getValidatingKey() throws Exception {
    return getKeyStore().getCertificate(configuration.getKeyStore().getAlias()).getPublicKey();
  }

  @Override
  public SecretKey getEncryptingKey() throws Exception {
    return (SecretKey) getKeyStore().getKey(configuration.getKeyStore().getEncAlias(), configuration.getKeyStore().getEncKeyPassword().toCharArray());
  }

  private KeyStore getKeyStore() throws Exception {
    KeyStore ks = KeyStore.getInstance("JCEKS");
    try (InputStream readStream = new FileInputStream(configuration.getKeyStore().getPath())) {
      char keyPassword[] = configuration.getKeyStore().getPassword().toCharArray();
      ks.load(readStream, keyPassword);
      return ks;
    }
  }
}
