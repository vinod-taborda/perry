package gov.ca.cwds.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

/**
 * Created by dmitry.rudenko on 6/30/2017.
 */
public class JwtService {
  private static final String IDENTITY_CLAIM = "identity";

  private KeyProvider keyProvider;
  private JwtConfiguration configuration;

  public JwtService(JwtConfiguration configuration) {
    this.configuration = configuration;
    this.keyProvider = new JCEKSKeyProvider(configuration);
  }

  public String generate(String id, String subject, String identity) throws JwtException {
    try {
      JWTClaimsSet claimsSet = prepareClaims(id, subject, identity);
      SignedJWT signedJWT = sign(claimsSet);
      String token;
      if (configuration.isEncryptionEnabled()) {
        JWEObject jweObject = encrypt(signedJWT);
        token = jweObject.serialize();
      } else {
        token = signedJWT.serialize();
      }
      return removeHeader(token);
    } catch (Exception e) {
      throw new JwtException(e);
    }
  }

  public String validate(String token) throws JwtException {
    try {
      String tokenWithHeader = addHeader(token);
      SignedJWT signedJWT;
      if (configuration.isEncryptionEnabled()) {
        signedJWT = decrypt(tokenWithHeader);
      } else {
        signedJWT = SignedJWT.parse(tokenWithHeader);
      }
      validateSignature(signedJWT);
      validateClaims(signedJWT.getJWTClaimsSet());
      return signedJWT.getJWTClaimsSet().getStringClaim(IDENTITY_CLAIM);
    } catch (Exception e) {
      throw new JwtException(e);
    }
  }

  private JWEObject encrypt(SignedJWT signedJWT) throws JwtException {
    try {
      JWEObject jweObject = new JWEObject(
              jweHeader(),
              new Payload(signedJWT));

      jweObject.encrypt(new DirectEncrypter(keyProvider.getEncryptingKey().getEncoded()));
      return jweObject;
    } catch (Exception e) {
      throw new JwtException(e);
    }
  }

  private JWEHeader jweHeader() {
    return new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.parse(configuration.getEncryptionMethod()))
            .contentType("JWT")
            .build();
  }

  private SignedJWT sign(JWTClaimsSet claimsSet) throws JwtException {
    try {
      JWSSigner signer = new RSASSASigner(keyProvider.getSigningKey());
      SignedJWT signedJWT = new SignedJWT(jwsHeader(), claimsSet);
      signedJWT.sign(signer);
      return signedJWT;
    } catch (Exception e) {
      throw new JwtException(e);
    }
  }

  private JWSHeader jwsHeader() {
    return new JWSHeader(JWSAlgorithm.RS256);
  }

  private JWTClaimsSet prepareClaims(String id, String subject, String identity) {
    long nowMillis = new Date().getTime();
    return new JWTClaimsSet.Builder()
            .subject(subject)
            .issueTime(new Date(nowMillis))
            .issuer(configuration.getIssuer())
            .expirationTime(new Date(nowMillis + configuration.getTimeout() * 60 * 1000))
            .jwtID(id)
            .claim(IDENTITY_CLAIM, identity).build();
  }

  private void validateSignature(SignedJWT signedJWT) throws JwtException {
    boolean verified = false;
    try {
      verified = signedJWT.verify(new RSASSAVerifier((RSAPublicKey) keyProvider.getValidatingKey()));
    } catch (Exception e) {
      throw new JwtException(e);
    }
    if (!verified) {
      fail();
    }
  }

  private SignedJWT decrypt(String token) throws JwtException {
    try {
      SignedJWT signedJWT;
      JWEObject jweObject = JWEObject.parse(token);
      jweObject.decrypt(new DirectDecrypter(keyProvider.getEncryptingKey().getEncoded()));
      signedJWT = jweObject.getPayload().toSignedJWT();
      return signedJWT;
    } catch (Exception e) {
      throw new JwtException(e);
    }
  }

  private void validateClaims(JWTClaimsSet claims) throws GeneralSecurityException {
    if ((configuration.getIssuer() != null && !configuration.getIssuer().equals(claims.getIssuer())) ||
            new Date().after(claims.getExpirationTime()) ||
            claims.getClaim(IDENTITY_CLAIM) == null) {
      fail();
    }
  }

  private String removeHeader(String token) {
    if (configuration.isHeadless()) {
      return token.substring(token.indexOf("."));
    }
    return token;
  }

  private String addHeader(String token) {
    if (configuration.isHeadless()) {
      Header header = configuration.isEncryptionEnabled() ? jweHeader() : jwsHeader();
      return header.toBase64URL().toString() + token;
    }
    return token;
  }

  private void fail() throws JwtException {
    throw new JwtException("Token validation failed");
  }
}
