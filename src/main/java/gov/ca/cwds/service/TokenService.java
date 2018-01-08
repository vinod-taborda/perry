package gov.ca.cwds.service;

import gov.ca.cwds.UniversalUserToken;
import gov.ca.cwds.data.reissue.TokenRepository;
import gov.ca.cwds.data.reissue.model.PerryTokenEntity;
import gov.ca.cwds.rest.api.domain.PerryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TPT2 on 10/27/2017.
 */
@Service
@Transactional("tokenTransactionManager")
public class TokenService {
  @Value("${perry.jwt.timeout:10}")
  private int accessCodeTimeout;
  private TokenRepository tokenRepository;
  private RandomValueStringGenerator generator = new RandomValueStringGenerator();

  public String issueAccessCode(UniversalUserToken userToken, OAuth2AccessToken accessToken) {
    String accessCode = generator.generate();
    PerryTokenEntity perryTokenEntity = new PerryTokenEntity();
    perryTokenEntity.setUser(userToken.getUserId());
    perryTokenEntity.setAccessCode(accessCode);
    perryTokenEntity.writeAccessToken(accessToken);
    perryTokenEntity.setToken(userToken.getToken());
    tokenRepository.deleteByUser(userToken.getUserId());
    tokenRepository.save(perryTokenEntity);
    return accessCode;
  }

  public String getAccessTokenByAccessCode(String accessCode) {
    List<PerryTokenEntity> tokens = tokenRepository.findByAccessCode(accessCode);
    if (tokens.isEmpty()) {
      throw new PerryException("Access Code: " + accessCode + " is not found");
    }
    if (tokens.size() > 1) {
      tokenRepository.delete(tokens);
      throw new PerryException("Access Code: " + accessCode + " is not unique");
    }
    PerryTokenEntity perryTokenEntity = tokens.get(0);
    long expirationTime = perryTokenEntity.getCreatedDate().getTime() + accessCodeTimeout * 60 * 1000;
    if (System.currentTimeMillis() > expirationTime) {
      tokenRepository.delete(perryTokenEntity);
      throw new PerryException("Access Code: " + accessCode + " is expired");
    }
    perryTokenEntity.setAccessCode(null);
    return perryTokenEntity.getToken();
  }

  public void updateAccessToken(String token, OAuth2AccessToken accessToken) {
    tokenRepository.updateAccessToken(token, (Serializable) accessToken);
  }

  public OAuth2AccessToken deleteToken(String token) {
    PerryTokenEntity perryTokenEntity = tokenRepository.findOne(token);
    OAuth2AccessToken accessToken = perryTokenEntity.readAccessToken();
    tokenRepository.delete(token);
    return accessToken;
  }

  public OAuth2AccessToken getAccessTokenByPerryToken(String token) {
    PerryTokenEntity perryTokenEntity = tokenRepository.findOne(token);
    return perryTokenEntity.readAccessToken();
  }

  @Autowired
  public void setTokenRepository(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }
}
