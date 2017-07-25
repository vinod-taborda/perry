package gov.ca.cwds.security.jwt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by dmitry.rudenko on 7/25/2017.
 */
public class JCEKSKeyProviderTest {
  private JwtConfiguration configuration;
  private KeyProvider keyProvider;

  @Before
  public void before() throws Exception {
    configuration = new JwtConfiguration();
    //JWT
    configuration.setTimeout(30);
    configuration.setIssuer("issuer");
    configuration.setKeyStore(new JwtConfiguration.KeyStoreConfiguration());
    //KeyStore
    configuration.getKeyStore().setPath(getClass().getResource("/security/enc.jceks").getPath());
    configuration.getKeyStore().setPassword("test");
    //Sign/Validate Key
    configuration.getKeyStore().setAlias("test");
    configuration.getKeyStore().setKeyPassword("test");
    //Enc Key
    configuration.setEncryptionEnabled(true);
    configuration.getKeyStore().setEncKeyPassword("test");
    configuration.getKeyStore().setEncAlias("enc128");
    configuration.setEncryptionMethod("A128GCM");
    keyProvider = new JCEKSKeyProvider(configuration);

  }

  @Test
  public void testGetValidatingKey() {
    PublicKey key = keyProvider.getValidatingKey();
    Assert.assertNotNull(key);
  }

  @Test
  public void testGetEncryptingKey() {
    SecretKey key = keyProvider.getEncryptingKey();
    Assert.assertNotNull(key);
  }

  @Test
  public void testGetSigningKey() {
    PrivateKey key = keyProvider.getSigningKey();
    Assert.assertNotNull(key);
  }
}
