package gov.ca.cwds.security.jwt;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by dmitry.rudenko on 6/30/2017.
 */
interface KeyProvider {
  PrivateKey getSigningKey() throws Exception;

  PublicKey getValidatingKey() throws Exception;

  SecretKey getEncryptingKey() throws Exception;
}
