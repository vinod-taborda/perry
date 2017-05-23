package gov.ca.cwds.service;

import gov.ca.cwds.PerryConfiguration;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Date;
import java.util.Set;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@Service
public class JwtService {
    private static final String IDENTITY_CLAIM = "identity";

    @Autowired
    PerryConfiguration perryConfiguration;
    @Autowired
    OAuth2ClientContext oAuth2ClientContext;

    public String generateToken(String subject, String accessToken, String identity) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
        JwtBuilder builder = Jwts.builder().setId(accessToken);
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Claims claims = new DefaultClaims();
        claims.setId(accessToken);
        claims.setIssuedAt(now);
        claims.setSubject(subject);
        claims.setExpiration(new Date(nowMillis + perryConfiguration.getJwt().getTimeout() * 60 * 1000));
        claims.setIssuer(perryConfiguration.getJwt().getIssuer());
        claims.setSubject(subject);
        claims.put(IDENTITY_CLAIM,  identity);
        builder.setClaims(claims)
                .signWith(signatureAlgorithm, getSigningKey());
        return builder.compact();
    }

    public String validateToken(String token) throws Exception {
        return (String) Jwts.parser().setSigningKey(getPublicKey()).
                requireIssuer(perryConfiguration.getJwt().getIssuer()).
                requireId(oAuth2ClientContext.getAccessToken().getValue()).
                parseClaimsJws(token).getBody().get(IDENTITY_CLAIM);
    }
    private Key getSigningKey() throws Exception {
        return getKeyStore().getKey(perryConfiguration.getKeyStore().getAlias(),
                perryConfiguration.getKeyStore().getKeyPassword().toCharArray());
    }

    private Key getPublicKey() throws Exception {
        return getKeyStore().getCertificate(perryConfiguration.getKeyStore().getAlias()).getPublicKey();
    }

    private KeyStore getKeyStore() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        try (InputStream readStream = new FileInputStream(perryConfiguration.getKeyStore().getPath())) {
            char keyPassword[] = perryConfiguration.getKeyStore().getPassword().toCharArray();
            ks.load(readStream, keyPassword);
            return ks;
        }
    }
}
